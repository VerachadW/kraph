package me.lazmaid.kraph.test

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import me.lazmaid.kraph.lang.relay.InputArgument
import me.lazmaid.kraph.lang.relay.Mutation
import me.lazmaid.kraph.lang.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class GraphQLPrintSpek : Spek({

    describe("Argument print function") {
        given("id as argument and value as 1") {
            val node = Argument(mapOf("id" to 1))
            on("print pretty") {
                it("should print (id: 1)") {
                    assertThat(node.print(true, 0), equalTo("(id: 1)"))
                }
            }
            on("print normal") {
                it("should print (id: 1)") {
                    assertThat(node.print(false, 0), equalTo("(id: 1)"))
                }
            }
        }
        given("money as argument and value as 123.45") {
            val node = Argument(mapOf("money" to 123.45))
            on("print pretty") {
                it("should print (money: 123.45)") {
                    assertThat(node.print(true, 0), equalTo("(money: 123.45)"))
                }
            }
            on("print normal") {
                it("should print (money: 123.45)") {
                    assertThat(node.print(false, 0), equalTo("(money: 123.45)"))
                }
            }
        }
        given("isCancel as argument and value as false") {
            val node = Argument(mapOf("isCancel" to false))
            on("print pretty") {
                it("should print (isCancel: false)") {
                    assertThat(node.print(true, 0), equalTo("(isCancel: false)"))
                }
            }
            on("print normal") {
                it("should print (isCancel: false)") {
                    assertThat(node.print(false, 0), equalTo("(isCancel: false)"))
                }
            }
        }
        given("id and title as arguments and value as 1 and Kraph") {
            val node = Argument(mapOf("id" to 1, "title" to "Kraph"))
            on("print pretty") {
                it("should print (id: 1, title: \"Kraph\")") {
                    assertThat(node.print(true, 0), equalTo("(id: 1, title: \"Kraph\")"))
                }
            }
            on("print normal") {
                it("should print (id: 1, title: \\\"Kraph\\\")") {
                    assertThat(node.print(false, 0), equalTo("(id: 1, title: \\\"Kraph\\\")"))
                }
            }
        }
        given("an array of string as argument") {
            val node = Argument(mapOf("titles" to listOf("title1", "title2")))
            on("print pretty") {
                it("should print (titles: [\"title1\", \"title2\"]") {
                    assertThat(node.print(true, 0), equalTo("(titles: [\"title1\", \"title2\"])"))
                }
            }
            on("print normal") {
                it("should print (titles: [\\\"title1\\\", \\\"title2\\\"]") {
                    assertThat(node.print(false, 0), equalTo("(titles: [\\\"title1\\\", \\\"title2\\\"])"))
                }
            }
        }
        given("an user object as argument") {
            val user = mapOf("name" to "John Doe", "email" to "john.doe@test.com")
            val node = Argument(mapOf("user" to user))
            on("print pretty") {
                it("should print (user: {\"name\": \"John Doe\", \"email\": \"john.doe@test.com\"})") {
                    assertThat(node.print(true, 0), equalTo("(user: {\"name\": \"John Doe\", \"email\": \"john.doe@test.com\"})"))
                }
            }
            on("print normal") {
                it("should print (user: {\\\"name\\\": \\\"John Doe\\\", \\\"email\\\": \\\"john.doe@test.com\\\"})") {
                    assertThat(node.print(false, 0), equalTo("(user: {\\\"name\\\": \\\"John Doe\\\", \\\"email\\\": \\\"john.doe@test.com\\\"})"))
                }
            }
        }
        given("an array of user object as argument") {
            val user1 = mapOf("name" to "user1", "email" to "user1@test.com")
            val user2 = mapOf("name" to "user2", "email" to "user2@test.com")
            val node = Argument(mapOf("users" to listOf(user1, user2)))
            on("print pretty") {
                it("should print (users: [{\"name\": \"user1\", \"email\": \"user1@test.com\"}, {\"name\": \"user2\", \"email\": \"user2@test.com\"}])") {
                    assertThat(node.print(true, 0), equalTo("(users: [{\"name\": \"user1\", \"email\": \"user1@test.com\"}, {\"name\": \"user2\", \"email\": \"user2@test.com\"}])"))
                }
            }
            on("print pretty") {
                it("should print (users: [{\\\"name\\\": \\\"user1\\\", \\\"email\\\": \\\"user1@test.com\\\"}, {\\\"name\": \\\"user2\\\", \\\"email\\\": \\\"user2@test.com\\\"}])") {
                    assertThat(node.print(false, 0), equalTo("(users: [{\\\"name\\\": \\\"user1\\\", \\\"email\": \\\"user1@test.com\\\"}, {\\\"name\\\": \\\"user2\\\", \\\"email\\\": \\\"user2@test.com\\\"}])"))
                }
            }
        }
    }
    describe("SelectionSet print function") {
        given("two fields; id and title") {
            val fields = listOf(Field("id"), Field("title"))
            val node = SelectionSet(fields)
            on("print pretty") {
                it("should print {\n  id\n  title\n}") {
                    assertThat(node.print(true, 0), equalTo("{\n  id\n  title\n}"))
                }
            }
            on("print normal") {
                it("should print {id title}") {
                    assertThat(node.print(false, 0), equalTo("{\\nid\\ntitle\\n}"))
                }
            }
        }
        given("three fields; id, title, and assignee which contains name and email") {
            val assigneeSet = SelectionSet(listOf(Field("name"), Field("email")))
            val assigneeField = Field("assignee", selectionSet = assigneeSet)
            val fields = listOf(Field("id"), Field("title"), assigneeField)
            val node = SelectionSet(fields)
            on("print pretty") {
                it("should print {\n  id\n  title\n  assignee {\n    name\n    email\n  }\n}") {
                    assertThat(node.print(true, 0), equalTo("{\n  id\n  title\n  assignee {\n    name\n    email\n  }\n}"))
                }
            }
            on("print normal") {
                it("should print {\\nid\\ntitle\\nassignee {\\nname\\nemail\\n}\\n}") {
                    assertThat(node.print(false, 0), equalTo("{\\nid\\ntitle\\nassignee {\\nname\\nemail\\n}\\n}"))
                }
            }
        }
    }
    describe("Mutation print function") {
        given("name registerUser with email and password as argument and payload contains id and token") {
            val argNode = InputArgument(mapOf("email" to "abcd@efgh.com", "password" to "abcd1234"))
            val setNode = SelectionSet(listOf(Field("id"), Field("token")))
            val node = Mutation("registerUser", argNode, setNode)
            on("print pretty") {
                it("should print registerUser(input: { email: \"abcd@efgh.com\", password: \"abcd1234\" }){\n  id\n  token\n}") {
                    assertThat(node.print(true, 0), equalTo("registerUser(input: { email: \"abcd@efgh.com\", password: \"abcd1234\" }) {\n  id\n  token\n}"))
                }
            }
            on("print normal") {
                it("should print registerUser(input: { email: \\\"abcd@efgh.com\\\", password: \\\"abcd1234\\\" }){ id token }") {
                    assertThat(node.print(false, 0), equalTo("registerUser(input: { email: \\\"abcd@efgh.com\\\", password: \\\"abcd1234\\\" }) {\\nid\\ntoken\\n}"))
                }
            }
        }
    }
    describe("Field print function") {
        given("name id") {
            val node = Field("id")
            on("print pretty") {
                it("should print id") {
                    assertThat(node.print(true, 0), equalTo("id"))
                }
            }
            on("print normal") {
                it("should print id") {
                    assertThat(node.print(false, 0), equalTo("id"))
                }
            }
        }
        given("name avatarSize and size argument with value as 20") {
            val argNode = Argument(mapOf("size" to 20))
            val node = Field("avatarSize", arguments = argNode)
            on("print pretty") {
                it("should print avatarSize(size: 20)") {
                    assertThat(node.print(true, 0), equalTo("avatarSize(size: 20)"))
                }
            }
            on("print normal") {
                it("should print avatarSize(size: 20)") {
                    assertThat(node.print(false, 0), equalTo("avatarSize(size: 20)"))
                }
            }
        }
        given("name assignee that contains name and email") {
            val setNode = SelectionSet(listOf(Field("name"), Field("email")))
            val node = Field("assignee", selectionSet = setNode)
            on("print pretty") {
                it("should print assignee {\n  name\n  email\n}") {
                    assertThat(node.print(true, 0), equalTo("assignee {\n  name\n  email\n}"))
                }
            }
            on("print normal") {
                it("should print assignee {\\nname\\nemail\\n}") {
                    assertThat(node.print(false, 0), equalTo("assignee {\\nname\\nemail\\n}"))
                }
            }
        }
        given("name user and id argument with value as 10 and contains name and email") {
            val argNode = Argument(mapOf("id" to 10))
            val setNode = SelectionSet(listOf(Field("name"), Field("email")))
            val node = Field("user", argNode, setNode)
            on("print pretty") {
                it("should print user(id: 10) {\n  name\n  email\n") {
                    assertThat(node.print(true, 0), equalTo("user(id: 10) {\n  name\n  email\n}"))
                }
            }
            on("print normal") {
                it("should print user(id: 10) {\\nname\\nemail\\n}") {
                    assertThat(node.print(false, 0), equalTo("user(id: 10) {\\nname\\nemail\\n}"))
                }
            }
        }
    }
    describe("Operation print function") {
        given("query type and field named id") {
            val node = Operation(OperationType.QUERY, SelectionSet(listOf(Field("id"))))
            on("print pretty") {
                it("should print query {\n  id\n}") {
                    assertThat(node.print(true, 0), equalTo("query {\n  id\n}"))
                }
            }
            on("print normal") {
                it("should print query {\\nid\\n}") {
                    assertThat(node.print(false, 0), equalTo("query {\\nid\\n}"))
                }
            }
        }
        given("query type with name \"getTask\" and field id") {
            val node = Operation(OperationType.QUERY, name = "getTask", selectionSet = SelectionSet(listOf(Field("id"))))
            on("print pretty") {
                it("should print query getTask {\n  id\n}") {
                    assertThat(node.print(true, 0), equalTo("query getTask {\n  id\n}"))
                }
            }
            on("print normal") {
                it("should print query getTask {\\nid\\n}") {
                    assertThat(node.print(false, 0), equalTo("query getTask {\\nid\\n}"))
                }
            }
        }
        given("query type with name \"getTask\" and id(1234) as argument and field title") {
            val argNode = Argument(mapOf("id" to 1234))
            val node = Operation(OperationType.QUERY, name = "getTask", arguments = argNode, selectionSet= SelectionSet(listOf(Field("title"))))
            on("print pretty") {
                it("should print query getTask(id: 1234) {\n  title\n}") {
                    assertThat(node.print(true, 0), equalTo("query getTask(id: 1234) {\n  title\n}"))
                }
            }
            on("print normal") {
                it("should print query getTask(id: 1234) {\\ntitle\\n}") {
                    assertThat(node.print(false, 0), equalTo("query getTask(id: 1234) {\\ntitle\\n}"))
                }
            }
        }
    }
    describe("Document print function") {
        given("document with simple query") {
            val queryNode = Operation(OperationType.QUERY, selectionSet = SelectionSet(listOf(Field("id"))))
            val node = Document(queryNode)
            on("print pretty") {
                it("should print document {\"query\":\"query { id }\", \"variables\": null, \"operationName\": null}") {
                    assertThat(node.print(true, 0), equalTo("{\"query\": \"query {\n  id\n}\", \"variables\": null, \"operationName\": null}"))
                }
            }
            on("print normal") {
                it("should print document {\"query\":\"query { id }\", \"variables\": null, \"operationName\": null}") {
                    assertThat(node.print(false, 0), equalTo("{\"query\": \"query {\\nid\\n}\", \"variables\": null, \"operationName\": null}"))
                }
            }
        }
        given("document with query named getAllTasks") {
            val queryNode = Operation(OperationType.QUERY, name = "getAllTasks", selectionSet = SelectionSet(listOf(Field("id"))))
            val node = Document(queryNode)
            on("print pretty") {
                it("should print document {\"query\":\"query getAllTasks { id }\", \"variables\": null, \"operationName\": \"getAllTasks\"}") {
                    assertThat(node.print(true, 0), equalTo("{\"query\": \"query getAllTasks {\n  id\n}\", \"variables\": null, \"operationName\": \"getAllTasks\"}"))
                }
            }
            on("print normal") {
                it("should print document {\"query\":\"query getAllTasks { id }\", \"variables\": null, \"operationName\": \"getAllTasks\"}") {
                    assertThat(node.print(false, 0), equalTo("{\"query\": \"query getAllTasks {\\nid\\n}\", \"variables\": null, \"operationName\": \"getAllTasks\"}"))
                }
            }
        }
    }
})