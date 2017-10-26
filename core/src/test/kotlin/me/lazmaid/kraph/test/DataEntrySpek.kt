package me.lazmaid.kraph.test

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isA
import me.lazmaid.kraph.lang.DataEntry
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

class DataEntrySpek : Spek({
    describe("NonDecimalNumberDataEntry class") {
        given("value as 5 and print pretty is false") {
            val entry = DataEntry.NonDecimalNumberData(5)
            val valueString = entry.print(false)
            it("should print \"5\"") {
                assertThat(valueString, isA(equalTo("5")))
            }
        }
        given("value as 5 and print pretty is true") {
            val entry = DataEntry.NonDecimalNumberData(5)
            val valueString = entry.print(true)
            it("should print \"5\"") {
                assertThat(valueString, isA(equalTo("5")))
            }
        }
    }

    describe("DecimalNumberDataEntry class") {
        given("value as 5.0 and print pretty is false") {
            val entry = DataEntry.DecimalNumberData(5.0)
            val valueString = entry.print(false)
            it("should print \"5.0\"") {
                assertThat(valueString, isA(equalTo("5.0")))
            }
        }
        given("value as 5.0 and print pretty is true") {
            val entry = DataEntry.DecimalNumberData(5.0)
            val valueString = entry.print(true)
            it("should print \"5.0\"") {
                assertThat(valueString, isA(equalTo("5.0")))
            }
        }
    }

    describe("BooleanDataEntry class") {
        given("value as true and print pretty is false") {
            val entry = DataEntry.BooleanData(true)
            val valueString = entry.print(false)
            it("should print \"true\"") {
                assertThat(valueString, isA(equalTo("true")))
            }
        }
        given("value as true and print pretty is true") {
            val entry = DataEntry.BooleanData(true)
            val valueString = entry.print(true)
            it("should print \"true\"") {
                assertThat(valueString, isA(equalTo("true")))
            }
        }
    }

    describe("StringDataEntry class") {
        given("value as 'Hello World' and print pretty is false") {
            val entry = DataEntry.StringData("Hello World")
            val valueString = entry.print(false)
            it("should print \\\"Hello World\\\"") {
                assertThat(valueString, isA(equalTo("\\\"Hello World\\\"")))
            }
        }
        given("value as true and print pretty is true") {
            val entry = DataEntry.StringData("Hello World")
            val valueString = entry.print(true)
            it("should print \"Hello World\"") {
                assertThat(valueString, isA(equalTo("\"Hello World\"")))
            }
        }
    }

    describe("ArrayDataEntry class") {
        given("value as [1,2,3] and print pretty is false") {
            val values = listOf(1, 2, 3).map { DataEntry.NonDecimalNumberData(it) }
            val entry = DataEntry.ArrayData(values)
            val valueString = entry.print(false)
            it("should print [1, 2, 3]") {
                assertThat(valueString, isA(equalTo("[1, 2, 3]")))
            }
        }
        given("vale as [1,2,3] and print pretty is true") {
            val values = listOf(1, 2, 3).map { DataEntry.NonDecimalNumberData(it) }
            val entry = DataEntry.ArrayData(values)
            val valueString = entry.print(true)
            it("should print [1, 2, 3]") {
                assertThat(valueString, isA(equalTo("[1, 2, 3]")))
            }
        }
    }

    describe("ObjectDataEntry class") {
        given("with key-values and print pretty as false") {
            val values = listOf("name" to DataEntry.StringData("John Doe"),
                    "age" to DataEntry.NonDecimalNumberData(18))
            val entry = DataEntry.ObjectData(values)
            val valueString = entry.print(false)
            it("should print {\\\"name\\\": \\\"John Doe\\\", \\\"age\\\": 18}") {
                assertThat(valueString, isA(equalTo("{\\\"name\\\": \\\"John Doe\\\", \\\"age\\\": 18}")))
            }
        }
        given("with key-values and print pretty as true") {
            val values = listOf("name" to DataEntry.StringData("John Doe"),
                    "age" to DataEntry.NonDecimalNumberData(18))
            val entry = DataEntry.ObjectData(values)
            val valueString = entry.print(true)
            it("should print {\"name\": \"John Doe\", \"age\": 18}") {
                assertThat(valueString, isA(equalTo("{\"name\": \"John Doe\", \"age\": 18}")))
            }
        }
    }
})