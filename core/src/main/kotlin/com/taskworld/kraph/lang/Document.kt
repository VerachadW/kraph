package com.taskworld.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class Document(internal val operation: Operation) : GraphPrintable {
    override fun print(): String {
        val operationNamePart = operation.name?.let {
            "\"$it\""
        }
        val variablesPart = null
        return "{\"query\": \"${operation.print()}\", \"variables\": $variablesPart, \"operationName\": $operationNamePart}"
    }
}