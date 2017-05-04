package me.lazmaid.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class Document(init: Document.() -> Unit) : GraphQLNode() {

    init {
        init()
    }

    private lateinit var operation: Operation

    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        val operationNamePart = operation.name?.let {
            "\"$it\""
        }
        val variablesPart = null
        return "{\"query\": \"${operation.print(prettyFormat, previousLevel)}\", \"variables\": $variablesPart, \"operationName\": $operationNamePart}"
    }

    fun query(name: String? = null, selectionSet: SelectionSet.() -> Unit) {
        val set = SelectionSet(selectionSet)
        operation = Operation(OperationType.QUERY, selectionSet = set, name = name)
    }

    fun mutation(name: String? = null, selectionSet: SelectionSet.() -> Unit) {
        val set = SelectionSet(selectionSet)
        operation = Operation(OperationType.MUTATION, selectionSet = set, name = name)
    }

}