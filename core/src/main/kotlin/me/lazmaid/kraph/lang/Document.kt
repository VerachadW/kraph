package me.lazmaid.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class Document(internal val operation: Operation) : GraphQLNode() {
    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        val operationNamePart = operation.name?.let {
            "\"$it\""
        }
        val variablesPart = null
        return "{\"query\": \"${operation.print(prettyFormat, previousLevel)}\", \"variables\": $variablesPart, \"operationName\": $operationNamePart}"
    }
}