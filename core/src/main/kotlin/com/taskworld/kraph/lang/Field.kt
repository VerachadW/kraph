package com.taskworld.kraph.lang

/**
 * Created by VerachadW on 9/19/2016 AD.
 */

internal open class Field(internal val name: String, internal val arguments: Argument? = null, internal var selectionSet: SelectionSet? = null) : GraphQLNode() {
    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        val selectionSetPart = selectionSet?.let { " " + it.print(prettyFormat, previousLevel) } ?: ""
        return "$name${arguments?.print(prettyFormat, previousLevel) ?: ""}$selectionSetPart"
    }
}




