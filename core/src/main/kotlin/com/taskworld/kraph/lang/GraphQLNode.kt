package com.taskworld.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

abstract internal class GraphQLNode {
    var level = 0

    abstract fun print(prettyFormat: Boolean, previousLevel: Int): String

    fun getIndentString(level: Int) = "  ".repeat(level)
    fun getNewLineString(prettyFormat: Boolean) = if (prettyFormat) "\n" else "\\n"

    fun Map<String, Any>.print(prettyFormat: Boolean) =
            this.entries.foldIndexed("") { index, acc, entry ->
                var string = acc + "${entry.key}: ${
                when (entry.value) {
                    is String -> {
                        if (prettyFormat) {
                            "\"${entry.value}\""
                        } else {
                            "\\\"${entry.value}\\\""
                        }
                    }
                    else -> {
                        entry.value
                    }
                }}"
                if (index != this.size - 1) {
                    string += ", "
                }
                string
            }
}

