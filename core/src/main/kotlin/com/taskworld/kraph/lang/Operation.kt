package com.taskworld.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class Operation(internal val type: OperationType, internal val selectionSet: SelectionSet,
                         internal val name: String? = null, internal val arguments: Argument? = null) : GraphPrintable {
    override fun print(): String {
        val namePart = name?.let { " " + it } ?: ""
        val argumentPart = arguments?.print() ?: ""
        return "${type.name.toLowerCase()}$namePart$argumentPart ${selectionSet.print()}"
    }
}

internal enum class OperationType {
    QUERY,
    MUTATION
}