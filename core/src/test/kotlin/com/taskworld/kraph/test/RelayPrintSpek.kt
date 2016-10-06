package com.taskworld.kraph.test

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.taskworld.kraph.lang.Argument
import com.taskworld.kraph.lang.Field
import com.taskworld.kraph.lang.SelectionSet
import com.taskworld.kraph.lang.relay.CursorConnection
import com.taskworld.kraph.lang.relay.Edges
import com.taskworld.kraph.lang.relay.PageInfo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * Created by VerachadW on 10/3/2016 AD.
 */
@RunWith(JUnitPlatform::class)
class RelayPrintSpek : Spek({
    describe("Relay Edges print function") {
        given("edges with addtional field id") {
            val node = Edges(SelectionSet(listOf(Field("title"))), additionalField = listOf(Field("id")))
            it("should print edges { node { title } id }") {
                assertThat(node.print(), equalTo("edges {\\nnode {\\ntitle\\n}\\nid\\n}"))
            }
        }
        given("edges with id and title inside node object") {
            val node = Edges(SelectionSet(listOf(Field("id"), Field("title"))))
            it("should print edges { node { id title } }") {
                assertThat(node.print(), equalTo("edges {\\nnode {\\nid\\ntitle\\n}\\n}"))
            }
        }
    }
    describe("Relay PageInfo print function") {
        given("default pageInfo with hasNextPage and hasPreviousPage") {
            val node = PageInfo(SelectionSet(listOf(Field("hasNextPage"),Field("hasPreviousPage"))))
            it("should print pageInfo { hasNextPage hasPreviousPage }") {
                assertThat(node.print(), equalTo("pageInfo {\\nhasNextPage\\nhasPreviousPage\\n}"))
            }
        }
    }
    describe("Relay Cursor Connection print function") {
        given("cursor cursorConnection named notes with title in node object and only 10 items") {
            val selectionSet = SelectionSet(listOf(Edges(SelectionSet(listOf(Field("title"))))))
            val argsNode = Argument(mapOf("first" to 10))
            val node = CursorConnection("notes", argsNode, selectionSet)
            it("should print notes(first: 10) { edges { node { title } } }") {
                assertThat(node.print(), equalTo("notes(first: 10) {\\nedges {\\nnode {\\ntitle\\n}\\n}\\n}"))
            }
        }
        given("cursor cursorConnection named notes with title in node object and only next 10 items after cursor named 'abcd1234'") {
            val selectionSet = SelectionSet(listOf(Edges(SelectionSet(listOf(Field("title"))))))
            val argsNode = Argument(mapOf("first" to 10, "after" to "abcd1234"))
            val node = CursorConnection("notes", argsNode, selectionSet)
            it("should print notes(first: 10, before: \"abcd1234\") { edges { node { title } } }") {
                assertThat(node.print(), equalTo("notes(first: 10, after: \\\"abcd1234\\\") {\\nedges {\\nnode {\\ntitle\\n}\\n}\\n}"))
            }
        }
        given("cursor cursorConnection named notes with title in node object and only last 10 items") {
            val selectionSet = SelectionSet(listOf(Edges(SelectionSet(listOf(Field("title"))))))
            val argsNode = Argument(mapOf("last" to 10))
            val node = CursorConnection("notes", argsNode, selectionSet)
            it("should print notes(last: 10) { edges { node { title } } }") {
                assertThat(node.print(), equalTo("notes(last: 10) {\\nedges {\\nnode {\\ntitle\\n}\\n}\\n}"))
            }
        }
        given("cursor cursorConnection named notes with title in node object and only last 10 items before cursor named 'abcd1234'") {
            val selectionSet = SelectionSet(listOf(Edges(SelectionSet(listOf(Field("title"))))))
            val argsNode = Argument(mapOf("last" to 10, "before" to "abcd1234"))
            val node = CursorConnection("notes", argsNode, selectionSet)
            it("should print notes(last: 10, before: \"abcd1234\") { edges { node { title } } }") {
                assertThat(node.print(), equalTo("notes(last: 10, before: \\\"abcd1234\\\") {\\nedges {\\nnode {\\ntitle\\n}\\n}\\n}"))
            }
        }
        given("cursor cursorConnection named notes with PageInfo object") {
            val pageNode = PageInfo(SelectionSet(listOf(Field("hasNextPage"), Field("hasPreviousPage"))))
            val selectionSet = SelectionSet(listOf(Edges(SelectionSet(listOf(Field("title")))), pageNode))
            val argsNode = Argument(mapOf("first" to 10))
            val node = CursorConnection("notes", argsNode, selectionSet)
            it("should print notes(first: 10) { edges { node { title } } pageInfo { hasNextPage hasPreviousPage } }") {
                assertThat(node.print(), equalTo("notes(first: 10) {\\nedges {\\nnode {\\ntitle\\n}\\n}\\npageInfo {\\nhasNextPage\\nhasPreviousPage\\n}\\n}"))
            }
        }
    }
})