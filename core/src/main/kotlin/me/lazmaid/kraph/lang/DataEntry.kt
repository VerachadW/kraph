package me.lazmaid.kraph.lang

import javax.xml.crypto.Data

/**
 * Created by vwongsawangt on 8/6/2017 AD.
 */

internal sealed class DataEntry {
    abstract fun print(prettyFormat: Boolean): String

    class NonDecimalNumberData(val value: Long) : DataEntry() {
        override fun print(prettyFormat: Boolean) = value.toString()
    }

    class DecimalNumberData(val value: Double) : DataEntry() {
        override fun print(prettyFormat: Boolean) = value.toString()
    }

    class BooleanData(val value: Boolean) : DataEntry() {
        override fun print(prettyFormat: Boolean) = value.toString()
    }

    class StringData(val value: String) : DataEntry() {
        override fun print(prettyFormat: Boolean) =
                if (prettyFormat) {
                    "\"$value\""
                } else {
                    "\\\"$value\\\""
                }
    }

    class ArrayData(val values: List<DataEntry>) : DataEntry() {
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

    class ObjectData(val values: List<Pair<String, DataEntry>>) : DataEntry() {
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

