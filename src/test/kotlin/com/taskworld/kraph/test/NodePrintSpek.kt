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
    describe("ArgumentNode print()") {
        given("\"id\" as argument and value as 1") {
            val node = ArgumentsNode(mapOf("id" to 1))
            it("should print (id: 1)") {
                assertThat(node.print(), equalTo("(id: 1)"))
            }
        }
        given("\"id\" and \"title\" as arguments and value as 1 and \"Kraph\"") {
            val node = ArgumentsNode(mapOf("id" to 1, "title" to "Kraph"))
            it("should print (id: 1, title: \"Kraph\")") {
                assertThat(node.print(), equalTo("(id: 1, title: \"Kraph\")"))
            }
        }
    }
    describe("SelectionSetNode print()") {
        given("two fields; id and title") {
            val fields = listOf(FieldNode("id"), FieldNode("title"))
            val node = SelectionSetNode(fields)
            it("should print {id title}") {
                assertThat(node.print(), equalTo("{\r\nid\r\ntitle\r\n}"))
            }
        }
        given("three fields; id, title, and assignee which contains name and email") {
            val assigneeSet = SelectionSetNode(listOf(FieldNode("name"), FieldNode("email")))
            val assigneeField = FieldNode("assignee", selectionSet = assigneeSet)
            val fields = listOf(FieldNode("id"), FieldNode("title"), assigneeField)
            val node = SelectionSetNode(fields)
            it("should print {id title assignee {name email}}") {
                assertThat(node.print(), equalTo("{\r\nid\r\ntitle\r\nassignee {\r\nname\r\nemail\r\n}\r\n}"))
            }
        }
    }
    describe("FieldNode print()") {
        given("name id") {
            val node = FieldNode("id")
            it("should print id") {
                assertThat(node.print(), equalTo("id"))
            }
        }
        given("name avatarSize and size argument with value as 20") {
            val argNode = ArgumentsNode(mapOf("size" to 20))
            val node = FieldNode("avatarSize", arguments = argNode)
            it("should print avatarSize(size: 20)") {
                assertThat(node.print(), equalTo("avatarSize(size: 20)"))
            }
        }
        given("name assignee that contains name and email") {
            val setNode = SelectionSetNode(listOf(FieldNode("name"), FieldNode("email")))
            val node = FieldNode("assignee", selectionSet = setNode)
            it("should print assignee { name email }") {
                assertThat(node.print(), equalTo("assignee {\r\nname\r\nemail\r\n}"))
            }
        }
    }
    describe("OperationNode print()") {
        given("query type and field named id") {
            val node = OperationNode(OperationType.QUERY, listOf(FieldNode("id")))
            it("should print query { id }") {
                assertThat(node.print(), equalTo("query {\r\nid\r\n}"))
            }
        }
        given("query type with name \"getTask\" and field id") {
            val node = OperationNode(OperationType.QUERY, name = "getTask", fields = listOf(FieldNode("id")))
            it("should print query getTask { id }") {
                assertThat(node.print(), equalTo("query getTask {\r\nid\r\n}"))
            }
        }
        given("query type with name \"getTask\" and id(1234) as argument and field title") {
            val argNode = ArgumentsNode(mapOf("id" to 1234))
            val node = OperationNode(OperationType.QUERY, name = "getTask", arguments = argNode, fields = listOf(FieldNode("title")))
            it("should print query getTask(id: 1234) { title }") {
                assertThat(node.print(), equalTo("query getTask(id: 1234) {\r\ntitle\r\n}"))
            }
        }
    }
    describe("DocumentNode print()") {
        given("document with simple query") {
            val queryNode = OperationNode(OperationType.QUERY, fields = listOf(FieldNode("id")))
            val node = DocumentNode(queryNode)
            it("should print document {\"query\":\"query { id }\", \"variables\": null, \"operationName\": null}") {
                assertThat(node.print(), equalTo("{\"query\": \"query {\r\nid\r\n}\", \"variables\": null, \"operationName\": null}"))
            }
        }
        given("document with query named getAllTasks") {
            val queryNode = OperationNode(OperationType.QUERY, name = "getAllTasks", fields = listOf(FieldNode("id")))
            val node = DocumentNode(queryNode)
            it("should print document {\"query\":\"query getAllTasks { id }\", \"variables\": null, \"operationName\": \"getAllTasks\"}") {
                assertThat(node.print(), equalTo("{\"query\": \"query getAllTasks {\r\nid\r\n}\", \"variables\": null, \"operationName\": \"getAllTasks\"}"))
            }
        }
    }
})