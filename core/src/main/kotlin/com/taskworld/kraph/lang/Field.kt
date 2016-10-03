package com.taskworld.kraph.lang

/**
 * Created by VerachadW on 9/19/2016 AD.
 */

internal open class Field(internal val name: String, internal val arguments: Argument? = null, internal var selectionSet: SelectionSet? = null) : GraphPrintable {
    override fun print(): String {
        val selectionSetPart = selectionSet?.let { " " + it.print() } ?: ""
        return "$name${arguments?.print() ?: ""}$selectionSetPart"
    }
}




