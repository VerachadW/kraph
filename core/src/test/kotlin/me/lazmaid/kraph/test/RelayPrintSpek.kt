package me.lazmaid.kraph.test

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import me.lazmaid.kraph.lang.Argument
import me.lazmaid.kraph.lang.Field
import me.lazmaid.kraph.lang.SelectionSet
import me.lazmaid.kraph.lang.PrintFormat
import me.lazmaid.kraph.lang.relay.CursorConnection
import me.lazmaid.kraph.lang.relay.Edges
import me.lazmaid.kraph.lang.relay.InputArgument
import me.lazmaid.kraph.lang.relay.PageInfo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

/**
 * Created by VerachadW on 10/3/2016 AD.
 */
class RelayPrintSpek : Spek({
    data class Expectation(val normal: String, val pretty: String, val json: String)
    describe("Relay InputArgument") {
        val tests = listOf(
            Triple(
                mapOf("id" to 1),
                "one number argument",
                Expectation("(input: { id: 1 })", "(input: { id: 1 })", "(input: { id: 1 })")
            ),
            Triple(
                mapOf("name" to "John Doe"),
                "name string argument",
                Expectation(
                    "(input: { name: \"John Doe\" })",
                    "(input: { name: \"John Doe\" })",
                    "(input: { name: \\\"John Doe\\\" })"
                )
            )
        )
        for((args, title, expectation) in tests) {
            val arguments = InputArgument(args)
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
    describe("Relay Edges") {
        val tests = listOf(
            Triple(
                Edges(SelectionSet(listOf(Field("title"))), additionalField = listOf(Field("id"))),
                "edges with addtional field id",
                Expectation(
                    "edges { node { title } id }",
                    "edges {\n  node {\n    title\n  }\n  id\n}",
                    "edges { node { title } id }"
                )
            ),
            Triple(
                Edges(SelectionSet(listOf(Field("id"), Field("title")))),
                "edges with id and title inside node object",
                Expectation(
                    "edges { node { id title } }",
                    "edges {\n  node {\n    id\n    title\n  }\n}",
                    "edges { node { id title } }"
                )
            )
        )
        for((edge, title, expectation) in tests) {
            given(title) {
                it("should print correctly in NORMAL mode") {
                    assertThat(edge.print(PrintFormat.NORMAL, 0), equalTo(expectation.normal))
                }
                it("should print correctly in PRETTY mode") {
                    assertThat(edge.print(PrintFormat.PRETTY, 0), equalTo(expectation.pretty))
                }
                it("should print correctly in JSON mode") {
                    assertThat(edge.print(PrintFormat.JSON, 0), equalTo(expectation.json))
                }
            }
        }
    }
    describe("Relay PageInfo") {
        given("default pageInfo with hasNextPage and hasPreviousPage") {
            val node = PageInfo(SelectionSet(listOf(Field("hasNextPage"),Field("hasPreviousPage"))))
            it("should print correctly in NORMAL mode") {
                assertThat(node.print(PrintFormat.NORMAL, 0), equalTo("pageInfo { hasNextPage hasPreviousPage }"))
            }
            it("should print correctly in PRETTY mode") {
                assertThat(node.print(PrintFormat.PRETTY, 0), equalTo("pageInfo {\n  hasNextPage\n  hasPreviousPage\n}"))
            }
            it("should print correctly in JSON mode") {
                assertThat(node.print(PrintFormat.JSON, 0), equalTo("pageInfo { hasNextPage hasPreviousPage }"))
            }
        }
    }
    describe("Relay Cursor Connection") {
        val tests = listOf(
            Triple(
                CursorConnection(
                    "notes",
                    Argument(mapOf("first" to 10)),
                    SelectionSet(listOf(Edges(SelectionSet(listOf(Field("title"))))))
                ),
                "name notes, title in node object, and only 10 items",
                Expectation(
                    "notes (first: 10) { edges { node { title } } }",
                    "notes (first: 10) {\n  edges {\n    node {\n      title\n    }\n  }\n}",
                    "notes (first: 10) { edges { node { title } } }"
                )
            ),
            Triple(
                CursorConnection(
                    "notes",
                    Argument(mapOf("first" to 10, "after" to "abcd1234")),
                    SelectionSet(listOf(Edges(SelectionSet(listOf(Field("title"))))))
                ),
                "name notes, title in node object, and only next 10 items after 'abcd1234'",
                Expectation(
                    "notes (first: 10, after: \"abcd1234\") { edges { node { title } } }",
                    "notes (first: 10, after: \"abcd1234\") {\n  edges {\n    node {\n      title\n    }\n  }\n}",
                    "notes (first: 10, after: \\\"abcd1234\\\") { edges { node { title } } }"
                )
            ),
            Triple(
                CursorConnection(
                    "notes",
                    Argument(mapOf("last" to 10)),
                    SelectionSet(listOf(Edges(SelectionSet(listOf(Field("title"))))))
                ),
                "name  notes, title in node object, and only last 10 items",
                Expectation(
                    "notes (last: 10) { edges { node { title } } }",
                    "notes (last: 10) {\n  edges {\n    node {\n      title\n    }\n  }\n}",
                    "notes (last: 10) { edges { node { title } } }"
                )
            ),
            Triple(
                CursorConnection(
                    "notes",
                    Argument(mapOf("last" to 10, "before" to "abcd1234")),
                    SelectionSet(listOf(Edges(SelectionSet(listOf(Field("title"))))))
                ),
                "name notes with title in node object and only last 10 items before 'abcd1234'",
                Expectation(
                    "notes (last: 10, before: \"abcd1234\") { edges { node { title } } }",
                    "notes (last: 10, before: \"abcd1234\") {\n  edges {\n    node {\n      title\n    }\n  }\n}",
                    "notes (last: 10, before: \\\"abcd1234\\\") { edges { node { title } } }"
                )
            ),
            Triple(
                 CursorConnection(
                     "notes",
                     Argument(mapOf("first" to 10)),
                     SelectionSet(
                         listOf(
                             Edges(SelectionSet(listOf(Field("title")))),
                             PageInfo(SelectionSet(listOf(
                                 Field("hasNextPage"),
                                 Field("hasPreviousPage")
                             )))
                        )
                    )
                ),
                "name notes and a PageInfo object",
                Expectation(
                    "notes (first: 10) { edges { node { title } } pageInfo { hasNextPage hasPreviousPage } }",
                    "notes (first: 10) {\n  edges {\n    node {\n      title\n    }\n  }\n  pageInfo {\n    hasNextPage\n    hasPreviousPage\n  }\n}",
                    "notes (first: 10) { edges { node { title } } pageInfo { hasNextPage hasPreviousPage } }"
                )
            )
        )
        for((cursor, title, expectation) in tests) {
            given(title) {
                it("should print correctly in NORMAL mode") {
                    assertThat(cursor.print(PrintFormat.NORMAL, 0), equalTo(expectation.normal))
                }
                it("should print correctly in PRETTY mode") {
                    assertThat(cursor.print(PrintFormat.PRETTY, 0), equalTo(expectation.pretty))
                }
                it("should print correctly in JSON mode") {
                    assertThat(cursor.print(PrintFormat.JSON, 0), equalTo(expectation.json))
                }
            }
        }
    }
})
