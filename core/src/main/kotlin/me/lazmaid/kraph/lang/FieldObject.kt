package me.lazmaid.kraph.lang

/**
 * Created by vwongsawangt on 5/1/2017 AD.
 */

internal open class FieldWithSelectionSet(name: String, argument: Argument? = null, val selectionSet: SelectionSet) : Field(name = name, arguments = argument) {

    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        val selectionSetPart = " " + selectionSet.print(prettyFormat, previousLevel)
        return super.print(prettyFormat, previousLevel) + selectionSetPart
    }
}
