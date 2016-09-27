package com.taskworld.kraph

/**
 * Created by VerachadW on 9/19/2016 AD.
 */

interface Node {
    fun print(): String
}

internal class DocumentNode(internal val operation: OperationNode) : Node {
    override fun print(): String {
        val operationNamePart = operation.name?.let {
            "\"$it\""
        }
        val variablesPart = null
        return "{\"query\": \"${operation.print()}\", \"variables\": $variablesPart, \"operationName\": $operationNamePart}"
    }
}

internal class SelectionSetNode(internal val fields: List<FieldNode>) : Node {
    override fun print(): String {
        return "{\r\n${ fields.print() }}"
    }
}

internal class OperationNode(internal val type: OperationType, internal val fields: List<FieldNode>,
                             internal val name: String? = null, internal val arguments: ArgumentsNode? = null): Node {

    override fun print(): String {
        val namePart = name?.let { " " + it } ?: ""
        val argumentPart = arguments?.print() ?: ""
        return "${type.name.toLowerCase()}$namePart$argumentPart {\r\n${ fields.print() }}"
    }
}

internal class ArgumentsNode(internal val args: Map<String, Any> = mapOf()) : Node {

    override fun print(): String {
        return "(${args.entries.foldIndexed(""){ index, acc, entry ->
            var string = acc + "${entry.key}: ${
                if(entry.value is String) {
                   "\"" + entry.value +"\""
                } else {
                    entry.value
                }
            }"
            if (index != args.size - 1) {
                string += ", "
            }
            string
        }})"
    }

}

internal class FieldNode(internal val name: String, internal val arguments: ArgumentsNode? = null, internal val selectionSet: SelectionSetNode? = null): Node {
    override fun print(): String {
        val selectionSetPart = selectionSet?.let { " " + it.print() } ?: ""
        return "$name${ arguments?.print() ?: "" }$selectionSetPart"
    }
}

fun <T: Node> List<T>.print() =
    this.fold(""){ acc, node ->
        acc + node.print() + "\r\n"
    }

internal enum class OperationType {
    QUERY,
    MUTATION
}


