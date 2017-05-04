package me.lazmaid.kraph.lang

import me.lazmaid.kraph.NoFieldsInSelectionSetException

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class SelectionSet(init: SelectionSet.() -> Unit) : GraphQLNode() {
    val fields = arrayListOf<Field>()

    init {
        init()
        if (fields.isEmpty()) {
            throw NoFieldsInSelectionSetException("No field elements inside Selection Set")
        }
    }

    override fun print(prettyFormat: Boolean, previousLevel: Int): String {

        if (prettyFormat) level = previousLevel + 1 else level = 0
        return "{${
             getNewLineString(prettyFormat) + fields.fold("") { acc, node ->
                 acc + getIndentString(level) + node.print(prettyFormat, level) + getNewLineString(prettyFormat)
            } + getIndentString(previousLevel)
        }}"
    }

    fun field(name: String, argument: Argument? = null) {
        fields.add(Field(name, argument))
    }

    fun fieldObject(name: String, argument: Argument? = null, selectionSet: SelectionSet) {
        fields.add(FieldWithSelectionSet(name, argument, selectionSet))
    }

}