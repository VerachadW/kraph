package com.taskworld.kraph.test

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.taskworld.kraph.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class NodePrintSpek : Spek({
    describe("ArgumentNode print function") {
        given("id as argument and value as 1") {
            val node = ArgumentNode(mapOf("id" to 1))
            it("should print (id: 1)") {
                assertThat(node.print(), equalTo("(id: 1)"))
            }
        }
        given("id and title as arguments and value as 1 and \"KraphQL\"") {
            val node = ArgumentNode(mapOf("id" to 1, "title" to "KraphQL"))
            it("should print (id: 1, title: \"KraphQL\")") {
                assertThat(node.print(), equalTo("(id: 1, title: \"KraphQL\")"))
            }
        }
    }
    describe("MutationArgumentNode print function") {
        given("id as argument and value as 1") {
            val node = MutationArgumentNode(mapOf("id" to 1))
            it("should print (input: { id: 1 })") {
                assertThat(node.print(), equalTo("(input: { id: 1 })"))
            }
        }
    }
    describe("SelectionSetNode print function") {
        given("two fields; id and title") {
            val fields = listOf(FieldNode("id"), FieldNode("title"))
            val node = SelectionSetNode(fields)
            it("should print {id title}") {
                assertThat(node.print(), equalTo("{\\nid\\ntitle\\n}"))
            }
        }
        given("three fields; id, title, and assignee which contains name and email") {
            val assigneeSet = SelectionSetNode(listOf(FieldNode("name"), FieldNode("email")))
            val assigneeField = FieldNode("assignee", selectionSet = assigneeSet)
            val fields = listOf(FieldNode("id"), FieldNode("title"), assigneeField)
            val node = SelectionSetNode(fields)
            it("should print {id title assignee {name email}}") {
                assertThat(node.print(), equalTo("{\\nid\\ntitle\\nassignee {\\nname\\nemail\\n}\\n}"))
            }
        }
    }
    describe("MutationNode print function") {
        given("name registerUser with email and password as argument and payload contains id and token") {
            val argNode = MutationArgumentNode(mapOf("email" to "abcd@efgh.com", "password" to "abcd1234"))
            val setNode = SelectionSetNode(listOf(FieldNode("id"), FieldNode("token")))
            val node = MutationNode("registerUser", argNode, setNode)
            it("should print registerUser(input: {email: \"abcd@efgh.com\", password: \"abcd1234\"}){ id token }") {
                assertThat(node.print(), equalTo("registerUser(input: { email: \"abcd@efgh.com\", password: \"abcd1234\" }) {\\nid\\ntoken\\n}"))
            }
        }
    }
    describe("FieldNode print function") {
        given("name id") {
            val node = FieldNode("id")
            it("should print id") {
                assertThat(node.print(), equalTo("id"))
            }
        }
        given("name avatarSize and size argument with value as 20") {
            val argNode = ArgumentNode(mapOf("size" to 20))
            val node = FieldNode("avatarSize", arguments = argNode)
            it("should print avatarSize(size: 20)") {
                assertThat(node.print(), equalTo("avatarSize(size: 20)"))
            }
        }
        given("name assignee that contains name and email") {
            val setNode = SelectionSetNode(listOf(FieldNode("name"), FieldNode("email")))
            val node = FieldNode("assignee", selectionSet = setNode)
            it("should print assignee { name email }") {
                assertThat(node.print(), equalTo("assignee {\\nname\\nemail\\n}"))
            }
        }
        given("name user and id argument with value as 10 and contains name and email") {
            val argNode = ArgumentNode(mapOf("id" to 10))
            val setNode = SelectionSetNode(listOf(FieldNode("name"), FieldNode("email")))
            val node = FieldNode("user", argNode, setNode)
            it("should print user(id: 10){ name email }") {
                assertThat(node.print(), equalTo("user(id: 10) {\\nname\\nemail\\n}"))
            }
        }
    }
    describe("OperationNode print function") {
        given("query type and field named id") {
            val node = OperationNode(OperationType.QUERY, listOf(FieldNode("id")))
            it("should print query { id }") {
                assertThat(node.print(), equalTo("query {\\nid\\n}"))
            }
        }
        given("query type with name \"getTask\" and field id") {
            val node = OperationNode(OperationType.QUERY, name = "getTask", fields = listOf(FieldNode("id")))
            it("should print query getTask { id }") {
                assertThat(node.print(), equalTo("query getTask {\\nid\\n}"))
            }
        }
        given("query type with name \"getTask\" and id(1234) as argument and field title") {
            val argNode = ArgumentNode(mapOf("id" to 1234))
            val node = OperationNode(OperationType.QUERY, name = "getTask", arguments = argNode, fields = listOf(FieldNode("title")))
            it("should print query getTask(id: 1234) { title }") {
                assertThat(node.print(), equalTo("query getTask(id: 1234) {\\ntitle\\n}"))
            }
        }
    }
    describe("DocumentNode print function") {
        given("document with simple query") {
            val queryNode = OperationNode(OperationType.QUERY, fields = listOf(FieldNode("id")))
            val node = DocumentNode(queryNode)
            it("should print document {\"query\":\"query { id }\", \"variables\": null, \"operationName\": null}") {
                assertThat(node.print(), equalTo("{\"query\": \"query {\\nid\\n}\", \"variables\": null, \"operationName\": null}"))
            }
        }
        given("document with query named getAllTasks") {
            val queryNode = OperationNode(OperationType.QUERY, name = "getAllTasks", fields = listOf(FieldNode("id")))
            val node = DocumentNode(queryNode)
            it("should print document {\"query\":\"query getAllTasks { id }\", \"variables\": null, \"operationName\": \"getAllTasks\"}") {
                assertThat(node.print(), equalTo("{\"query\": \"query getAllTasks {\\nid\\n}\", \"variables\": null, \"operationName\": \"getAllTasks\"}"))
            }
        }
    }
})