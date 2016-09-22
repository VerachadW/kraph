package com.taskworld.kraph

/**
 * Created by VerachadW on 9/19/2016 AD.
 */

class QL(f: QL.() -> Unit) {
    internal lateinit var document: DocumentNode

    init {
        f.invoke(this)
    }

    fun query(name: String = "", builder: FieldBuilder.() -> Unit) {
        document = DocumentNode(OperationNode(OperationType.QUERY, fields = FieldBuilder().apply(builder).fields, name = name))
    }

    override fun toString(): String {
        return document.print()
    }

    inner class FieldBuilder() {
        internal val fields = arrayListOf<FieldNode>()
        internal var selectionSet: SelectionSetNode? = null

        fun fieldObject(name: String, params: Map<String, Any>? = null, builder: (FieldBuilder.() -> Unit)) {
            addField(name, params, builder)
        }

        fun field(name: String, params: Map<String, Any>? = null) {
            addField(name, params)
        }

        private fun addField(name: String, params: Map<String, Any>? = null, builder: (FieldBuilder.() -> Unit)? = null) {
            val args = params?.let { ArgumentsNode(it) }
            selectionSet = builder?.let {
                selectionSet(builder)
            }
            fields += FieldNode(name, arguments = args, selectionSet = selectionSet)
        }

        private fun selectionSet(f: FieldBuilder.() -> Unit): SelectionSetNode {
            val builder = FieldBuilder().apply(f)
            return SelectionSetNode(builder.fields)
        }
    }

}

