package com.taskworld.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class SelectionSet(internal val fields: List<Field>) : GraphQLNode() {

    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        if (prettyFormat) level = previousLevel + 1 else level = 0
        return "{${
             getNewLineString(prettyFormat) + fields.fold("") { acc, node ->
                 acc + getIndentString(level) + node.print(prettyFormat, level) + getNewLineString(prettyFormat)
            } + getIndentString(previousLevel)
        }}"
    }
}