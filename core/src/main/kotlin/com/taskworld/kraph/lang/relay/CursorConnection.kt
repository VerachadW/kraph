package com.taskworld.kraph.lang.relay

import com.taskworld.kraph.lang.Argument
import com.taskworld.kraph.lang.Field
import com.taskworld.kraph.lang.SelectionSet


/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class CursorConnection : Field {

    constructor(name: String, first: Int = -1, last: Int = -1, after: String? = null, before: String? = null,
                edges: Edges, pageInfo: PageInfo? = null) : super(name) {
        val args = linkedMapOf<String, Any>()

        if (first != -1) args.put("first", first)
        if (last != -1) args.put("last", last)
        before?.let { args.put("before", it) }
        after?.let { args.put("after", it) }

        if (args.isEmpty()) {
            throw IllegalArgumentException("There must be at least 1 argument for Cursor Connection")
        }

        arguments = Argument(args)

        val fields = arrayListOf<Field>()
        fields.add(edges)
        pageInfo?.let {
            fields.add(it)
        }

        selectionSet = SelectionSet(fields)
    }
}

internal class Edges : Field {

    constructor(nodeSelectionSet: SelectionSet, additionalField: List<Field> = listOf()) : super("edges") {
        val fields = listOf(Field("node", selectionSet = nodeSelectionSet), Field("cursor")) + additionalField
        selectionSet = SelectionSet(fields)
    }

}

internal class PageInfo() : Field("PageInfo") {
    init {
        selectionSet = SelectionSet(listOf(Field("hasNextPage"), Field("hasPreviousPage")))
    }
}

