package me.lazmaid.kraph.lang

import me.lazmaid.kraph.types.KraphVariable
import me.lazmaid.kraph.types.KraphVariableType

/**
 * Created by vwongsawangt on 8/6/2017 AD.
 */

internal sealed class DataEntry {
    abstract fun print(format: PrintFormat): String

    class NonDecimalNumberData(private val value: Long) : DataEntry() {
        constructor(value: Int) : this(value.toLong())

        override fun print(format: PrintFormat) = value.toString()
    }

    class DecimalNumberData(private val value: Double) : DataEntry() {
        override fun print(format: PrintFormat) = value.toString()
    }

    class BooleanData(private val value: Boolean) : DataEntry() {
        override fun print(format: PrintFormat) = value.toString()
    }

    class VariableData(private val value: KraphVariable) : DataEntry() {
        override fun print(format: PrintFormat) = value.dollarName
    }

    class VariableType(private val value: KraphVariableType) : DataEntry() {
        override fun print(format: PrintFormat) = value.value
    }

    class StringData(private val value: String) : DataEntry() {
        override fun print(format: PrintFormat) =
                if (format == PrintFormat.JSON) {
                    "\\\"$value\\\""
                } else {
                    "\"$value\""
                }
    }

    class ArrayData(private val values: List<DataEntry>) : DataEntry() {
        override fun print(format: PrintFormat) =
            "[${ values.joinToString(", ") { it.print(format) } }]"
    }

    class ObjectData(private val values: List<Pair<String, DataEntry>>) : DataEntry() {
        override fun print(format: PrintFormat) =
            "{${ values.joinToString(", ") { (k, v) ->
                "${k}: ${v.print(format)}"
            } }}"
    }
}
