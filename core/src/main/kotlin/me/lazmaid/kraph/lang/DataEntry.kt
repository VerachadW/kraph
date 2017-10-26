package me.lazmaid.kraph.lang

/**
 * Created by vwongsawangt on 8/6/2017 AD.
 */

internal sealed class DataEntry {
    abstract fun print(prettyFormat: Boolean): String

    class NonDecimalNumberData(private val value: Long) : DataEntry() {
        constructor(value: Int) : this(value.toLong())

        override fun print(prettyFormat: Boolean) = value.toString()
    }

    class DecimalNumberData(private val value: Double) : DataEntry() {
        override fun print(prettyFormat: Boolean) = value.toString()
    }

    class BooleanData(private val value: Boolean) : DataEntry() {
        override fun print(prettyFormat: Boolean) = value.toString()
    }

    class StringData(private val value: String) : DataEntry() {
        override fun print(prettyFormat: Boolean) =
                if (prettyFormat) {
                    "\"$value\""
                } else {
                    "\\\"$value\\\""
                }
    }

    class ArrayData(private val values: List<DataEntry>) : DataEntry() {
        override fun print(prettyFormat: Boolean) =
                values.foldIndexed("[") { index, acc, item ->
                    var newAcc = acc + item.print(prettyFormat)
                    if (index != values.size - 1) {
                        newAcc += ", "
                    } else {
                        newAcc += "]"
                    }
                    newAcc
                }
    }

    class ObjectData(private val values: List<Pair<String, DataEntry>>) : DataEntry() {
        override fun print(prettyFormat: Boolean) =
                values.foldIndexed("{") { index, acc, (k, v) ->
                    var newAcc = acc + "${k.wrappedWithQuotes(prettyFormat)}: ${v.print(prettyFormat)}"
                    if (index != values.size - 1) {
                        newAcc += ", "
                    } else {
                        newAcc += "}"
                    }
                    newAcc
                }
    }
}

