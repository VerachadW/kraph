package com.taskworld.kraph

import com.taskworld.kraph.lang.*
import com.taskworld.kraph.lang.relay.*

/**
 * Created by VerachadW on 9/19/2016 AD.
 */

class Kraph(f: Kraph.() -> Unit) {
    internal lateinit var document: Document

    init {
        f.invoke(this)
    }

    fun query(name: String? = null, builder: FieldBuilder.() -> Unit) {
        document = Document(Operation(OperationType.QUERY, fields = FieldBuilder().apply(builder).fields, name = name))
    }

    fun mutation(name: String? = null, builder: MutationBuilder.() -> Unit) {
        document = Document(Operation(OperationType.MUTATION, fields = MutationBuilder().apply(builder).mutations, name = name))
    }

    private fun selectionSet(f: FieldBuilder.() -> Unit): SelectionSet {
        val builder = FieldBuilder().apply(f)
        return SelectionSet(builder.fields)
    }

    override fun toString(): String {
        return document.print()
    }

    inner class MutationBuilder() {
        internal val mutations = arrayListOf<Mutation>()

        fun func(name: String, args: Map<String, Any>, builder: FieldBuilder.() -> Unit) {
            mutations += Mutation(name, InputArgument(args), selectionSet(builder))
        }

    }

    inner open class FieldBuilder() {
        internal val fields = arrayListOf<Field>()

        fun fieldObject(name: String, args: Map<String, Any>? = null, builder: (FieldBuilder.() -> Unit)) {
            addField(name, args, builder)
        }

        fun field(name: String, args: Map<String, Any>? = null) {
            addField(name, args)
        }

        protected fun addField(name: String, args: Map<String, Any>? = null, builder: (FieldBuilder.() -> Unit)? = null) {
            val argNode = args?.let(::Argument)
            val selectionSet = builder?.let {
                selectionSet(builder)
            }
            fields += Field(name, arguments = argNode, selectionSet = selectionSet)
        }

    }

    inner class CursorConnectionBuilder() : FieldBuilder() {
        fun connection(name: String, first: Int = -1, last: Int = -1,
                       before: String? = null, after: String? = null,
                       builder: (CursorSelectionBuilder.() -> Unit)) {

            val selection = CursorSelectionBuilder()
            builder.invoke(selection)

            fields += CursorConnection(name, first, last, before, after,
                    selection.edgesField, selection.pageInfoField)
        }
    }

    inner class CursorSelectionBuilder() {

        internal lateinit var edgesField : Edges
        internal var pageInfoField: PageInfo? = null

        fun edges(builder: NodeBuilder.() -> Unit) {
            val node = NodeBuilder()
            builder.invoke(node)
            edgesField = Edges(node.selectionSet, node.fields)
        }

        fun pageInfo() {
            pageInfoField = PageInfo()
        }
    }

    inner class NodeBuilder(): FieldBuilder() {
        internal lateinit var selectionSet: SelectionSet
        fun node(builder: FieldBuilder.() -> Unit) {
            selectionSet(builder)
        }

    }

}

