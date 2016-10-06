package com.taskworld.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

interface GraphPrintable {
    fun print(): String
}

internal fun <T : GraphPrintable> List<T>.print() =
        this.fold("") { acc, node ->
            acc + node.print() + "\\n"
        }

internal fun Map<String, Any>.print() =
        this.entries.foldIndexed("") { index, acc, entry ->
            var string = acc + "${entry.key}: ${
            when (entry.value) {
                is String -> {
                    "\\\"${entry.value}\\\""
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
