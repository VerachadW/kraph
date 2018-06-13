package me.lazmaid.kraph.lang

import me.lazmaid.kraph.types.KraphVariable
import me.lazmaid.kraph.types.KraphVariableType

abstract internal class GraphQLNode {
    var level = 0

    abstract fun print(format: PrintFormat, previousLevel: Int): String

    fun getIndentString(level: Int) = "  ".repeat(level)
    fun getNewLineString(format: PrintFormat) = if (format == PrintFormat.PRETTY) "\n" else " "

    fun print(value: Map<String, Any?>, format: PrintFormat) =
        value.entries.joinToString(", ") { (k, v) ->
            "$k: ${convertToDataEntry(v).print(format)}"
        }

    private fun convertToArrayData(value: List<*>): DataEntry.ArrayData =
        DataEntry.ArrayData(value.map(this::convertToDataEntry))

    private fun convertToObjectData(map: Map<String, *>): DataEntry.ObjectData =
        DataEntry.ObjectData(map.map {
            it.key to convertToDataEntry(it.value)
        })

    @Suppress("UNCHECKED_CAST")
    private fun convertToDataEntry(value: Any?) =
            when(value) {
                is String   -> DataEntry.StringData(value)
                is Int      -> DataEntry.NonDecimalNumberData(value.toLong())
                is Long     -> DataEntry.NonDecimalNumberData(value)
                is Float    -> DataEntry.DecimalNumberData(value.toDouble())
                is Boolean  -> DataEntry.BooleanData(value)
                is Double   -> DataEntry.DecimalNumberData(value)
                is KraphVariable -> DataEntry.VariableData(value)
                is KraphVariableType -> DataEntry.VariableType(value)
                is List<*>  -> convertToArrayData(value)
                is Map<*,*> -> convertToObjectData(value as Map<String, *>)
                else        -> throw RuntimeException("Unsupported Type: $value")
            }
}

internal fun String.wrappedWithQuotes(shouldBeEscaped: Boolean) =
        if (shouldBeEscaped) {
            "\"$this\""
        } else {
            "\\\"$this\\\""
        }

internal enum class PrintFormat {
    NORMAL,
    PRETTY,
    JSON
}
