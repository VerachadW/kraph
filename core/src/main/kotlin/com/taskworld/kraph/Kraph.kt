package com.taskworld.kraph

/**
 * Created by VerachadW on 9/19/2016 AD.
 */

class Kraph(f: Kraph.() -> Unit) {
    internal lateinit var document: DocumentNode

    init {
        f.invoke(this)
    }

    fun query(name: String? = null, builder: FieldBuilder.() -> Unit) {
        document = DocumentNode(OperationNode(OperationType.QUERY, fields = FieldBuilder().apply(builder).fields, name = name))
    }

    fun mutation(name: String? = null, builder: MutationBuilder.() -> Unit) {
        document = DocumentNode(OperationNode(OperationType.MUTATION, fields = MutationBuilder().apply(builder).mutations, name = name))
    }

    override fun toString(): String {
        return document.print()
    }

    inner class MutationBuilder() {
        internal val mutations = arrayListOf<MutationNode>()

        fun func(name: String, args: Map<String, Any>, builder: FieldBuilder.() -> Unit) {
            mutations += MutationNode(name, InputArgumentNode(args), selectionSet(builder))
        }

    }

    inner class FieldBuilder() {
        internal val fields = arrayListOf<FieldNode>()
        internal var selectionSet: SelectionSetNode? = null

        fun fieldObject(name: String, args: Map<String, Any>? = null, builder: (FieldBuilder.() -> Unit)) {
            addField(name, args, builder)
        }

        fun field(name: String, args: Map<String, Any>? = null) {
            addField(name, args)
        }

        private fun addField(name: String, args: Map<String, Any>? = null, builder: (FieldBuilder.() -> Unit)? = null) {
            val argNode = args?.let(::ArgumentNode)
            selectionSet = builder?.let {
                selectionSet(builder)
            }
            fields += FieldNode(name, arguments = argNode, selectionSet = selectionSet)
        }

    }

    private fun selectionSet(f: FieldBuilder.() -> Unit): SelectionSetNode {
        val builder = FieldBuilder().apply(f)
        return SelectionSetNode(builder.fields)
    }

}

