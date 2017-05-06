package me.lazmaid.kraph.lang

import me.lazmaid.kraph.NoFieldsInSelectionSetException
import me.lazmaid.kraph.lang.relay.*

internal typealias SelectionSetBuilder = SelectionSet.() -> Unit

open class SelectionSet(builder: SelectionSetBuilder) : GraphQLNode() {
    val fields = arrayListOf<Field>()

    init {
        builder.invoke(this)
        if (fields.isEmpty()) {
            throw NoFieldsInSelectionSetException("No field elements inside Selection Set")
        }
    }

    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        if (prettyFormat) level = previousLevel + 1 else level = 0
        return "{${
             getNewLineString(prettyFormat) + fields.fold("") { acc, node ->
                 acc + getIndentString(level) + node.print(prettyFormat, level) + getNewLineString(prettyFormat)
            } + getIndentString(previousLevel)
        }}"
    }

    operator internal fun plusAssign(field: Field) {
        fields += field
    }

    fun field(name: String, args: Map<String, Any>? = null) {
        fields.add(Field(name, if (args != null) Argument(args) else null ))
    }

    fun fieldObject(name: String, args: Map<String, Any>? = null, builder: SelectionSetBuilder) {
        fields.add(FieldWithSelectionSet(name, if (args!= null) Argument(args) else null, SelectionSet(builder)))
    }

    fun cursorConnection(name: String, first: Int = -1, last: Int = -1,
                         before: String? = null, after: String? = null,
                         builder: SelectionSetBuilder) {
        val argsMap = linkedMapOf<String, Any>()
        if (first != -1) argsMap.put("first", first)
        if (last != -1) argsMap.put("last", last)
        before?.let { argsMap.put("before", it) }
        after?.let { argsMap.put("after", it) }

        if (argsMap.isEmpty()) {
            throw IllegalArgumentException("There must be at least 1 argument for Cursor Connection")
        }

        fields += CursorConnection(name, Argument(argsMap), SelectionSet(builder))
    }

    fun func(name: String, args: Map<String, Any>, builder: SelectionSetBuilder) {
        fields += Mutation(name, InputArgument(args), SelectionSet(builder))
    }

    fun edges(builder: SelectionSetBuilder) {
        fields += EdgesField(SelectionSet(builder))
    }

    fun node(builder: SelectionSetBuilder) {
        fields += NodeField(SelectionSet(builder))
    }

    fun pageInfo(builder: SelectionSetBuilder) {
        fields += PageInfo(SelectionSet(builder))
    }
}