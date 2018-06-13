package me.lazmaid.kraph.test

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import me.lazmaid.kraph.lang.*
import me.lazmaid.kraph.lang.relay.InputArgument
import me.lazmaid.kraph.lang.relay.Mutation
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

class GraphQLPrintSpek : Spek({
    data class Expectation(val normal: String, val pretty: String, val json: String)
    describe("Argument") {
        val tests = listOf(
            Triple(
                mapOf("id" to 1),
                "a single number argument",
                Expectation("(id: 1)", "(id: 1)", "(id: 1)")
            ),
            Triple(
                mapOf("money" to 123.45),
                "a single decimal argument",
                Expectation("(money: 123.45)", "(money: 123.45)", "(money: 123.45)")
            ),
            Triple(
                mapOf("isCancel" to false),
                "a single boolean argument",
                Expectation("(isCancel: false)", "(isCancel: false)", "(isCancel: false)")
            ),
            Triple(
                mapOf("id" to 1, "title" to "Kraph"),
                "a number and a string argument",
                Expectation(
                    "(id: 1, title: \"Kraph\")",
                    "(id: 1, title: \"Kraph\")",
                    "(id: 1, title: \\\"Kraph\\\")"
                )
            ),
            Triple(
                mapOf("user" to mapOf("name" to "John Doe", "email" to "john.doe@test.com")),
                "an object argument",
                Expectation(
                    "(user: {name: \"John Doe\", email: \"john.doe@test.com\"})",
                    "(user: {name: \"John Doe\", email: \"john.doe@test.com\"})",
                    "(user: {name: \\\"John Doe\\\", email: \\\"john.doe@test.com\\\"})"
                )
            ),
            Triple(
                mapOf("users" to
                    listOf(
                        mapOf("name" to "user1", "email" to "user1@test.com"),
                        mapOf("name" to "user2", "email" to "user2@test.com")
                    )
                ),
                "an array of objects argument",
                Expectation(
                    "(users: [{name: \"user1\", email: \"user1@test.com\"}, {name: \"user2\", email: \"user2@test.com\"}])",
                    "(users: [{name: \"user1\", email: \"user1@test.com\"}, {name: \"user2\", email: \"user2@test.com\"}])",
                    "(users: [{name: \\\"user1\\\", email: \\\"user1@test.com\\\"}, {name: \\\"user2\\\", email: \\\"user2@test.com\\\"}])"
                )
            )
        )
        for((args, title, expectation) in tests) {
            val arguments = Argument(args)
            given(title) {
                it("should print correctly in NORMAL mode") {
                    assertThat(arguments.print(PrintFormat.NORMAL, 0), equalTo(expectation.normal))
                }
                it("should print correctly in PRETTY mode") {
                    assertThat(arguments.print(PrintFormat.PRETTY, 0), equalTo(expectation.pretty))
                }
                it("should print correctly in JSON mode") {
                    assertThat(arguments.print(PrintFormat.JSON, 0), equalTo(expectation.json))
                }
            }
        }
    }
    describe("SelectionSet") {
        val tests = listOf(
            Triple(
                listOf(Field("id"), Field("title")),
                "two fields; id and title",
                Expectation(
                    "{ id title }",
                    "{\n  id\n  title\n}",
                    "{ id title }"
                )
            ),
            Triple(
                listOf(Field("id"), Field("title"), Field("assignee", selectionSet = SelectionSet(listOf(Field("name"), Field("email"))))),
                "three fields; id, title, and assignee which contains name and email",
                Expectation(
                    "{ id title assignee { name email } }",
                    "{\n  id\n  title\n  assignee {\n    name\n    email\n  }\n}",
                    "{ id title assignee { name email } }"
                )
            )
            // TODO: add tests for fragment syntax once fragments use GraphQL fragments
        )
        for((args, title, expectation) in tests) {
            val selectionSet = SelectionSet(args)
            given(title) {
                it("should print correctly in NORMAL mode") {
                    assertThat(selectionSet.print(PrintFormat.NORMAL, 0), equalTo(expectation.normal))
                }
                it("should print correctly in PRETTY mode") {
                    assertThat(selectionSet.print(PrintFormat.PRETTY, 0), equalTo(expectation.pretty))
                }
                it("should print correctly in JSON mode") {
                    assertThat(selectionSet.print(PrintFormat.JSON, 0), equalTo(expectation.json))
                }
            }
        }
    }
    describe("Mutation") {
        given("name RegisterUser with email and password as argument and payload contains id and token") {
            val argNode = InputArgument(mapOf("email" to "abcd@efgh.com", "password" to "abcd1234"))
            val setNode = SelectionSet(listOf(Field("id"), Field("token")))
            val node = Mutation("RegisterUser", argNode, setNode)
            it("should print correctly in NORMAL mode") {
                assertThat(node.print(PrintFormat.NORMAL, 0), equalTo("RegisterUser (input: { email: \"abcd@efgh.com\", password: \"abcd1234\" }) { id token }"))
            }
            it("should print correctly in PRETTY mode") {
                assertThat(node.print(PrintFormat.PRETTY, 0), equalTo("RegisterUser (input: { email: \"abcd@efgh.com\", password: \"abcd1234\" }) {\n  id\n  token\n}"))
            }
            it("should print correctly in JSON mode") {
                assertThat(node.print(PrintFormat.JSON, 0), equalTo("RegisterUser (input: { email: \\\"abcd@efgh.com\\\", password: \\\"abcd1234\\\" }) { id token }"))
            }
        }
    }
    describe("Field") {
        val tests = listOf(
            Triple(Field("id"), "name id", Expectation("id", "id", "id")),
            Triple(
                Field("avatarSize", Argument(mapOf("size" to 20))),
                "name avatarSize and size argument with value as 20",
                Expectation(
                    "avatarSize (size: 20)",
                    "avatarSize (size: 20)",
                    "avatarSize (size: 20)"
                )
            ),
            Triple(
                Field("assignee", selectionSet = SelectionSet(listOf(Field("name"), Field("email")))),
                "name assignee and containing name and email",
                Expectation(
                    "assignee { name email }",
                    "assignee {\n  name\n  email\n}",
                    "assignee { name email }"
                )
            ),
            Triple(
                Field("user", Argument(mapOf("id" to 10)), SelectionSet(listOf(Field("name"), Field("email")))),
                "name user and id argument with value as 10 and containing name and email",
                Expectation(
                    "user (id: 10) { name email }",
                    "user (id: 10) {\n  name\n  email\n}",
                    "user (id: 10) { name email }"
                )
            )
        )
        for((field, title, expectation) in tests) {
            given(title) {
                it("should print correctly in NORMAL mode") {
                    assertThat(field.print(PrintFormat.NORMAL, 0), equalTo(expectation.normal))
                }
                it("should print correctly in PRETTY mode") {
                    assertThat(field.print(PrintFormat.PRETTY, 0), equalTo(expectation.pretty))
                }
                it("should print correctly in JSON mode") {
                    assertThat(field.print(PrintFormat.JSON, 0), equalTo(expectation.json))
                }
            }
        }
    }
    val tests = listOf(
        Triple(
            Operation(OperationType.QUERY, SelectionSet(listOf(Field("id")))),
            "type query and a field named id",
            Expectation(
                "query { id }",
                "query {\n  id\n}",
                "query { id }"
            )
        ),
        Triple(
            Operation(OperationType.QUERY, name = "getTask", selectionSet = SelectionSet(listOf(Field("id")))),
            "type query with name \"getTask\" and field id",
            Expectation(
                "query getTask { id }",
                "query getTask {\n  id\n}",
                "query getTask { id }"
            )
        ),
        Triple(
            Operation(OperationType.QUERY, name = "getTask", arguments = Argument(mapOf("id" to 1234)), selectionSet = SelectionSet(listOf(Field("title")))),
            "type query with name \"getTask\" and id(1234) as argument and field title",
            Expectation(
                "query getTask (id: 1234) { title }",
                "query getTask (id: 1234) {\n  title\n}",
                "query getTask (id: 1234) { title }"
            )
        )
    )
    describe("Operation") {
        for((operation, title, expectation) in tests) {
            given(title) {
                it("should print correctly in NORMAL mode") {
                    assertThat(operation.print(PrintFormat.NORMAL, 0), equalTo(expectation.normal))
                }
                it("should print correctly in PRETTY mode") {
                    assertThat(operation.print(PrintFormat.PRETTY, 0), equalTo(expectation.pretty))
                }
                it("should print correctly in JSON mode") {
                    assertThat(operation.print(PrintFormat.JSON, 0), equalTo(expectation.json))
                }
            }
        }
    }
    describe("Document") {
        for((operation, title, expectation) in tests) {
            given(title) {
                it("should print always JSON format in the JSON wrapper") {
                    assertThat(Document(operation, Variables()).print(PrintFormat.NORMAL, 0),
                            equalTo("{\"query\": \"${expectation.json}\", \"variables\": null, \"operationName\": ${operation.name?.let {"\"$it\""} ?: "null"}}"))
                }
            }
        }
    }
})
