package me.lazmaid.kraph.test

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import me.lazmaid.kraph.lang.DataEntry
import me.lazmaid.kraph.lang.PrintFormat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

class DataEntrySpek : Spek({
    data class Expectation(val normal: String, val pretty: String, val json: String)
    val tests = listOf(
        Pair(DataEntry.NonDecimalNumberData(5), Expectation("5", "5", "5")),
        Pair(DataEntry.DecimalNumberData(5.0), Expectation("5.0", "5.0", "5.0")),
        Pair(DataEntry.BooleanData(true), Expectation("true", "true", "true")),
        Pair(DataEntry.StringData("Hello world"), Expectation("\"Hello world\"", "\"Hello world\"", "\\\"Hello world\\\"")),
        Pair(DataEntry.ArrayData(listOf(1, 2, 3).map { DataEntry.NonDecimalNumberData(it) }), Expectation("[1, 2, 3]", "[1, 2, 3]", "[1, 2, 3]")),
        Pair(DataEntry.ObjectData(
            listOf(
                "name" to DataEntry.StringData("John Doe"),
                "age" to DataEntry.NonDecimalNumberData(18))
            ),
            Expectation(
                "{name: \"John Doe\", age: 18}",
                "{name: \"John Doe\", age: 18}",
                "{name: \\\"John Doe\\\", age: 18}"
            )
        )
    )
    for((node, expectation) in tests) {
        describe("${node::class.simpleName}") {
            it("should print correctly in NORMAL mode") {
                assertThat(node.print(PrintFormat.NORMAL), equalTo(expectation.normal))
            }
            it("should print correctly in PRETTY mode") {
                assertThat(node.print(PrintFormat.PRETTY), equalTo(expectation.pretty))
            }
            it("should print correctly in JSON mode") {
                assertThat(node.print(PrintFormat.JSON), equalTo(expectation.json))
            }
        }
    }
})
