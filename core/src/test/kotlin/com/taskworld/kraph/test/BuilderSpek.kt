package com.taskworld.kraph.test

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.natpryce.hamkrest.isA
import com.taskworld.kraph.Kraph
import com.taskworld.kraph.lang.OperationType
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
        given("smaple query") {
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
                assertThat(query.document.operation.fields, hasSize(equalTo(1)))
            }
            it("should have field named notes inside query") {
                assertThat(query.document.operation.fields[0].name, equalTo("notes"))
            }
            it("should have four fields inside note object") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields, hasSize(equalTo(4)))
            }
            it("should have field named id inside notes object") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields[0].name, equalTo("id"))
            }
            it("should have field named content inside notes object") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields[1].name, equalTo("content"))
            }
            it("should have field named author inside notes object") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields[2].name, equalTo("author"))
            }
            it("should have field named avatarUrl inside notes object") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields[3].name, equalTo("avatarUrl"))
            }
            it("should have size argument with value 100 for avatarUrl field") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields[3].arguments!!.args["size"] as Int, equalTo(100))
            }
            it("should have two fields inside author object") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields[2].selectionSet!!.fields, hasSize(equalTo(2)))
            }
            it("should have field named name inside author object") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields[2].selectionSet!!.fields[0].name, equalTo("name"))
            }
            it("should have field named email inside author object") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields[2].selectionSet!!.fields[1].name, equalTo("email"))
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
                assertThat(query.document.operation.fields, hasSize(equalTo(1)))
            }
            it("should have mutation named registerUser") {
                assertThat(query.document.operation.fields[0].name, equalTo("registerUser"))
            }
            it("should have 3 arguments in registerUser mutation") {
                assertThat(query.document.operation.fields[0].arguments!!.args.entries, hasSize(equalTo(3)))
            }
            it("should have argument in registerUser mutation with named email and value as abcd@efgh.com") {
                assertThat(query.document.operation.fields[0].arguments!!.args["email"] as String, equalTo("abcd@efgh.com"))
            }
            it("should have argument in registerUser mutation with named password and value as abcd1234") {
                assertThat(query.document.operation.fields[0].arguments!!.args["password"] as String, equalTo("abcd1234"))
            }
            it("should have argument in registerUser mutation with named age and value as 30") {
                assertThat(query.document.operation.fields[0].arguments!!.args["age"] as Int, equalTo(30))
            }
            it("should contains 2 field in registerUser payload") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields, hasSize(equalTo(2)))
            }
            it("should have id field in registerUser payload") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields[0].name, equalTo("id"))
            }
            it("should have token field in mutation payload") {
                assertThat(query.document.operation.fields[0].selectionSet!!.fields[1].name, equalTo("token"))
            }
        }
    }
})