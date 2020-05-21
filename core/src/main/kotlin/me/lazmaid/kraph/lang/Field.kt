package me.lazmaid.kraph.lang

/**
 * Created by VerachadW on 9/19/2016 AD.
 */

internal open class Field(
    internal val name: String,
    internal val alias: String? = null,
    internal val arguments: Argument? = null,
    internal var selectionSet: SelectionSet? = null
) : GraphQLNode() {
    override fun print(
        format: PrintFormat,
        previousLevel: Int
    ): String {
        val alias = alias?.let { "$it: " } ?: ""
        val selectionSetPart = selectionSet?.print(format, previousLevel)?.let{ " $it" } ?: ""
        val argumentsPart = arguments?.print(format, previousLevel)?.let{ " $it" } ?: ""
        return "$alias$name$argumentsPart$selectionSetPart"
    }
}
