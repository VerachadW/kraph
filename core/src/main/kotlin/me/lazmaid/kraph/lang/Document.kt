package me.lazmaid.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class Document(internal val operation: Operation, internal val variables: Variables) : GraphQLNode() {
    override fun print(
        format: PrintFormat,
        previousLevel: Int
    ): String {
        val operationNamePart = operation.name?.let{ "\"$it\"" }
        val variablesPart = variables.takeUnless { it.isEmpty() }?.run { print(PrintFormat.JSON, previousLevel) }
        return "{\"query\": \"${operation.print(PrintFormat.JSON, previousLevel)}\", \"variables\": $variablesPart, \"operationName\": $operationNamePart}"
    }
}
