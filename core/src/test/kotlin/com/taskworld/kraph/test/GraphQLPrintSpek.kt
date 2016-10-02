package com.taskworld.kraph.test

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.taskworld.kraph.lang.*
import com.taskworld.kraph.lang.relay.CursorConnection
import com.taskworld.kraph.lang.relay.Edges
import com.taskworld.kraph.lang.relay.InputArgument
import com.taskworld.kraph.lang.relay.Mutation
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class GraphQLPrintSpek : Spek({
    describe("Argument print function") {
        given("id as argument and value as 1") {
            val node = Argument(mapOf("id" to 1))
            it("should print (id: 1)") {
                assertThat(node.print(), equalTo("(id: 1)"))
            }
        }
        given("id and title as arguments and value as 1 and Kraph") {
            val node = Argument(mapOf("id" to 1, "title" to "Kraph"))
            it("should print (id: 1, title: \\\"Kraph\\\")") {
                assertThat(node.print(), equalTo("(id: 1, title: \\\"Kraph\\\")"))
            }
        }
    }
    describe("InputArgument print function") {
        given("id as argument and value as 1") {
            val node = InputArgument(mapOf("id" to 1))
            it("should print (input: { id: 1 })") {
                assertThat(node.print(), equalTo("(input: { id: 1 })"))
            }
        }
        given("name as argument and value as John Doe") {
            val node = InputArgument(mapOf("name" to "John Doe"))
            it("should print (input: { name: \\\"John Doe\\\" })") {
                assertThat(node.print(), equalTo("(input: { name: \\\"John Doe\\\" })"))
            }
        }
    }
    describe("SelectionSet print function") {
        given("two fields; id and title") {
            val fields = listOf(Field("id"), Field("title"))
            val node = SelectionSet(fields)
            it("should print {id title}") {
                assertThat(node.print(), equalTo("{\\nid\\ntitle\\n}"))
            }
        }
        given("three fields; id, title, and assignee which contains name and email") {
            val assigneeSet = SelectionSet(listOf(Field("name"), Field("email")))
            val assigneeField = Field("assignee", selectionSet = assigneeSet)
            val fields = listOf(Field("id"), Field("title"), assigneeField)
            val node = SelectionSet(fields)
            it("should print {id title assignee {name email}}") {
                assertThat(node.print(), equalTo("{\\nid\\ntitle\\nassignee {\\nname\\nemail\\n}\\n}"))
            }
        }
    }
    describe("Mutation print function") {
        given("name registerUser with email and password as argument and payload contains id and token") {
            val argNode = InputArgument(mapOf("email" to "abcd@efgh.com", "password" to "abcd1234"))
            val setNode = SelectionSet(listOf(Field("id"), Field("token")))
            val node = Mutation("registerUser", argNode, setNode)
            it("should print registerUser(input: {email: \\\"abcd@efgh.com\\\", password: \\\"abcd1234\\\"}){ id token }") {
                assertThat(node.print(), equalTo("registerUser(input: { email: \\\"abcd@efgh.com\\\", password: \\\"abcd1234\\\" }) {\\nid\\ntoken\\n}"))
            }
        }
    }
    describe("Field print function") {
        given("name id") {
            val node = Field("id")
            it("should print id") {
                assertThat(node.print(), equalTo("id"))
            }
        }
        given("name avatarSize and size argument with value as 20") {
            val argNode = Argument(mapOf("size" to 20))
            val node = Field("avatarSize", arguments = argNode)
            it("should print avatarSize(size: 20)") {
                assertThat(node.print(), equalTo("avatarSize(size: 20)"))
            }
        }
        given("name assignee that contains name and email") {
            val setNode = SelectionSet(listOf(Field("name"), Field("email")))
            val node = Field("assignee", selectionSet = setNode)
            it("should print assignee { name email }") {
                assertThat(node.print(), equalTo("assignee {\\nname\\nemail\\n}"))
            }
        }
        given("name user and id argument with value as 10 and contains name and email") {
            val argNode = Argument(mapOf("id" to 10))
            val setNode = SelectionSet(listOf(Field("name"), Field("email")))
            val node = Field("user", argNode, setNode)
            it("should print user(id: 10){ name email }") {
                assertThat(node.print(), equalTo("user(id: 10) {\\nname\\nemail\\n}"))
            }
        }
    }
    describe("Relay Connection print function") {
        given("connection named notes with title in node object and only 10 items") {
            val edgesNode = Edges(SelectionSet(listOf(Field("title"))))
            val node = CursorConnection("notes", first = 10, edges = edgesNode)
            it("should print notes(first: 10) { edges { node { title } cursor } }") {
                assertThat(node.print(), equalTo("notes(first: 10) {\\nedges {\\nnode {\\ntitle\\n}\\ncursor\\n}\\n}"))
            }
        }
        given("connection named notes with title in node object and only next 10 items after cursor named 'abcd1234'") {
            val edgesNode = Edges(SelectionSet(listOf(Field("title"))))
            val node = CursorConnection("notes", first = 10, after= "abcd1234", edges = edgesNode)
            it("should print notes(first: 10, before: \"abcd1234\") { edges { node { title } cursor } }") {
                assertThat(node.print(), equalTo("notes(first: 10, after: \\\"abcd1234\\\") {\\nedges {\\nnode {\\ntitle\\n}\\ncursor\\n}\\n}"))
            }
        }
        given("connection named notes with title in node object and only last 10 items") {
            val edgesNode = Edges(SelectionSet(listOf(Field("title"))))
            val node = CursorConnection("notes", last = 10, edges = edgesNode)
            it("should print notes(last: 10) { edges { node { title } cursor } }") {
                assertThat(node.print(), equalTo("notes(last: 10) {\\nedges {\\nnode {\\ntitle\\n}\\ncursor\\n}\\n}"))
            }
        }
        given("connection named notes with title in node object and only last 10 items before cursor named 'abcd1234'") {
            val edgesNode = Edges(SelectionSet(listOf(Field("title"))))
            val node = CursorConnection("notes", last = 10, before = "abcd1234", edges = edgesNode)
            it("should print notes(last: 10, before: \"abcd1234\") { edges { node { title } cursor } }") {
                assertThat(node.print(), equalTo("notes(last: 10, before: \\\"abcd1234\\\") {\\nedges {\\nnode {\\ntitle\\n}\\ncursor\\n}\\n}"))
            }
        }
    }

    describe("Operation print function") {
        given("query type and field named id") {
            val node = Operation(OperationType.QUERY, listOf(Field("id")))
            it("should print query { id }") {
                assertThat(node.print(), equalTo("query {\\nid\\n}"))
            }
        }
        given("query type with name \"getTask\" and field id") {
            val node = Operation(OperationType.QUERY, name = "getTask", fields = listOf(Field("id")))
            it("should print query getTask { id }") {
                assertThat(node.print(), equalTo("query getTask {\\nid\\n}"))
            }
        }
        given("query type with name \"getTask\" and id(1234) as argument and field title") {
            val argNode = Argument(mapOf("id" to 1234))
            val node = Operation(OperationType.QUERY, name = "getTask", arguments = argNode, fields = listOf(Field("title")))
            it("should print query getTask(id: 1234) { title }") {
                assertThat(node.print(), equalTo("query getTask(id: 1234) {\\ntitle\\n}"))
            }
        }
    }
    describe("Document print function") {
        given("document with simple query") {
            val queryNode = Operation(OperationType.QUERY, fields = listOf(Field("id")))
            val node = Document(queryNode)
            it("should print document {\"query\":\"query { id }\", \"variables\": null, \"operationName\": null}") {
                assertThat(node.print(), equalTo("{\"query\": \"query {\\nid\\n}\", \"variables\": null, \"operationName\": null}"))
            }
        }
        given("document with query named getAllTasks") {
            val queryNode = Operation(OperationType.QUERY, name = "getAllTasks", fields = listOf(Field("id")))
            val node = Document(queryNode)
            it("should print document {\"query\":\"query getAllTasks { id }\", \"variables\": null, \"operationName\": \"getAllTasks\"}") {
                assertThat(node.print(), equalTo("{\"query\": \"query getAllTasks {\\nid\\n}\", \"variables\": null, \"operationName\": \"getAllTasks\"}"))
            }
        }
    }
})