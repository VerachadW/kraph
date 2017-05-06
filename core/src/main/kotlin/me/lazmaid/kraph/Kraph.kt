package me.lazmaid.kraph

import me.lazmaid.kraph.lang.*
import me.lazmaid.kraph.lang.relay.*

class Kraph(init: Kraph.() -> Unit) {

    init {
        init()
    }

    internal lateinit var document: Document

    fun toGraphQueryString() = document.operation.print(true, 0)
    fun toRequestString() = document.print(false, 0)

    fun query(name: String? = null, selectionSet: SelectionSetBuilder) {
        val set = SelectionSet(selectionSet)
        document = Document(Operation(OperationType.QUERY, selectionSet = set, name = name))
    }

    fun mutation(name: String? = null, selectionSet: SelectionSetBuilder) {
        val set = SelectionSet(selectionSet)
        document = Document(Operation(OperationType.MUTATION, selectionSet = set, name = name))
    }
}


