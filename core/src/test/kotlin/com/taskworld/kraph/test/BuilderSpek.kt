package com.taskworld.kraph.test

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import com.taskworld.kraph.Kraph
import com.taskworld.kraph.NoFieldsInSelectionSetException
import com.taskworld.kraph.lang.OperationType
import com.taskworld.kraph.lang.relay.CursorConnection
import com.taskworld.kraph.lang.relay.PageInfo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * Created by VerachadW on 9/20/2016 AD.
 */
@RunWith(JUnitPlatform::class)
class BuilderSpek : Spek({
    describe("Kraph Query DSL Builder") {
        given("sample query") {
            val query = Kraph {
                query("getAllNotes") {
                    fieldObject("notes") {
                        field("id")
                        field("content")
                        fieldObject("author") {
                            field("name")
                            field("email")
                        }
                        field("avatarUrl", args = mapOf("size" to 100))
                    }
                }
            }
            it("should have query operation type") {
                assertThat(query.document.operation.type, isA(equalTo(OperationType.QUERY)))
            }
            it("should have only one field inside query") {
                assertThat(query.document.operation.selectionSet.fields, hasSize(equalTo(1)))
            }
            it("should have field named notes inside query") {
                assertThat(query.document.operation.selectionSet.fields[0].name, equalTo("notes"))
            }
            it("should have four fields inside note object") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields, hasSize(equalTo(4)))
            }
            it("should have field named id inside notes object") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[0].name, equalTo("id"))
            }
            it("should have field named content inside notes object") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[1].name, equalTo("content"))
            }
            it("should have field named author inside notes object") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[2].name, equalTo("author"))
            }
            it("should have field named avatarUrl inside notes object") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[3].name, equalTo("avatarUrl"))
            }
            it("should have size argument with value 100 for avatarUrl field") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[3].arguments!!.args["size"] as Int, equalTo(100))
            }
            it("should have two fields inside author object") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[2].selectionSet!!.fields, hasSize(equalTo(2)))
            }
            it("should have field named name inside author object") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[2].selectionSet!!.fields[0].name, equalTo("name"))
            }
            it("should have field named email inside author object") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[2].selectionSet!!.fields[1].name, equalTo("email"))
            }
        }
        given("sample query with no field in selection set") {
            it("should throw NoFieldsInSelectionSetException") {
                assertThat({
                    Kraph {
                        query {
                        }
                    }
                }, throws(noFieldInSelectionSetMessageMatcher("query")))
            }
        }
        given("sample query with object and no fields") {
            it("should throw NoFieldsInSelectionSetException") {
                assertThat({
                    Kraph {
                        query {
                            fieldObject("test") { }
                        }
                    }
                }, throws(noFieldInSelectionSetMessageMatcher("test")))
            }
        }
        given("sample mutation") {
            val query = Kraph {
                mutation {
                    func("registerUser", mapOf("email" to "abcd@efgh.com", "password" to "abcd1234", "age" to 30)) {
                        field("id")
                        field("token")
                    }
                }
            }
            it("should have mutation operation type") {
                assertThat(query.document.operation.type, isA(equalTo(OperationType.MUTATION)))
            }
            it("should have only 1 mutation") {
                assertThat(query.document.operation.selectionSet.fields, hasSize(equalTo(1)))
            }
            it("should have mutation named registerUser") {
                assertThat(query.document.operation.selectionSet.fields[0].name, equalTo("registerUser"))
            }
            it("should have 3 arguments in registerUser mutation") {
                assertThat(query.document.operation.selectionSet.fields[0].arguments!!.args.entries, hasSize(equalTo(3)))
            }
            it("should have argument in registerUser mutation with named email and value as abcd@efgh.com") {
                assertThat(query.document.operation.selectionSet.fields[0].arguments!!.args["email"] as String, equalTo("abcd@efgh.com"))
            }
            it("should have argument in registerUser mutation with named password and value as abcd1234") {
                assertThat(query.document.operation.selectionSet.fields[0].arguments!!.args["password"] as String, equalTo("abcd1234"))
            }
            it("should have argument in registerUser mutation with named age and value as 30") {
                assertThat(query.document.operation.selectionSet.fields[0].arguments!!.args["age"] as Int, equalTo(30))
            }
            it("should contains 2 field in registerUser payload") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields, hasSize(equalTo(2)))
            }
            it("should have id field in registerUser payload") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[0].name, equalTo("id"))
            }
            it("should have token field in mutation payload") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[1].name, equalTo("token"))
            }
        }
        given("sample mutation with no field in selection set") {
            it("should throw NoFieldsInSelectionSetException") {
                assertThat({
                    Kraph {
                        mutation { }
                    }
                }, throws(noFieldInSelectionSetMessageMatcher("mutation")))
            }
        }
        given("sample query with cursor connection first 10 items after cursor a1") {
            val query = Kraph {
                query {
                    connection("users", first = 10, after = "a1") {
                        edges {
                            node {
                                field("id")
                            }
                            field("custom")
                        }
                    }
                }
            }
            it("should have cursor connection field") {
                assertThat(query.document.operation.selectionSet.fields[0] is CursorConnection, equalTo(true))
            }
            it("should have argument first with value 10 for cursor connection") {
                assertThat(query.document.operation.selectionSet.fields[0].arguments!!.args["first"] as Int, equalTo(10))
            }
            it("should have argument after with value a1 for cursor connection") {
                assertThat(query.document.operation.selectionSet.fields[0].arguments!!.args["after"] as String, equalTo("a1"))
            }
            it("should have custom field inside edges block") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[0].selectionSet!!.fields[1].name, equalTo("custom"))
            }
        }
        given("sample query with cursor connection last 10 items before cursor a2 and PageInfo object") {
            val query = Kraph {
                query {
                    connection("users", last = 10, before = "a2") {
                        edges {
                            node {
                                field("id")
                            }
                        }
                        pageInfo {
                            field("hasNextPage")
                        }
                        field("custom")
                    }
                }
            }
            it("should have cursor connection field") {
                assertThat(query.document.operation.selectionSet.fields[0] is CursorConnection, equalTo(true))
            }
            it("should have argument first with value 10 for cursor connection") {
                assertThat(query.document.operation.selectionSet.fields[0].arguments!!.args["last"] as Int, equalTo(10))
            }
            it("should have argument after with value a1 for cursor connection") {
                assertThat(query.document.operation.selectionSet.fields[0].arguments!!.args["before"] as String, equalTo("a2"))
            }
            it("should have custom field next to edges block") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[2].name, equalTo("custom"))
            }
            it("should have pageInfo object") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[1] is PageInfo, equalTo(true))
            }
            it("should have hasNextPage field in pageInfo object") {
                assertThat(query.document.operation.selectionSet.fields[0].selectionSet!!.fields[1].selectionSet!!.fields[0].name, equalTo("hasNextPage"))
            }
        }
        given("sample query with cursor connection without arguments") {
            it("should throw IllegalArgumentException with message \"There must be at least 1 argument for Cursor Connection\"") {
                assertThat({
                Kraph {
                    query {
                        connection("users") {
                            edges {
                                node {
                                    field("title")
                                }
                            }
                        }
                    }
                }
                }, throws<IllegalArgumentException>(cursorEmptyArgumentsMessageMatcher))
            }
        }
        given("sample query with pageInfo without hasNextPage and has PreviousPage") {
            it("should throw IllegalArgumentException with message \"Selection Set must contain hasNextPage and/or hasPreviousPage field\"") {
                assertThat({
                    Kraph {
                        query {
                            connection("users", first = 10) {
                                edges {
                                    node {
                                        field("title")
                                    }
                                }
                                pageInfo {
                                    field("nextPage")
                                }
                            }
                        }
                    }
                }, throws<NoFieldsInSelectionSetException>(pageInfoNoValidFieldMessageMatcher))
            }
        }
    }
})

val cursorEmptyArgumentsMessageMatcher = Matcher(Exception::checkExceptionMessage, "There must be at least 1 argument for Cursor Connection")
val pageInfoNoValidFieldMessageMatcher = Matcher(Exception::checkExceptionMessage, "Selection Set must contain hasNextPage and/or hasPreviousPage field")
fun noFieldInSelectionSetMessageMatcher(name: String) = Matcher(Exception::checkExceptionMessage, "No field elements inside \"$name\" block")

fun Exception.checkExceptionMessage(message: String) = this.message == message