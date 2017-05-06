package me.lazmaid.kraph.lang.relay

import me.lazmaid.kraph.lang.*

internal class CursorConnection(name: String, args: Argument, selectionSet: SelectionSet) : FieldWithSelectionSet(name, args, selectionSet)

internal class EdgesField(selectionSet: SelectionSet) : FieldWithSelectionSet("edges", selectionSet = selectionSet) {
    init {
        selectionSet.fields.add(CursorField())
    }
    inner class CursorField : Field("cursor")
}

internal class NodeField(selectionSet: SelectionSet) : FieldWithSelectionSet("node", selectionSet = selectionSet)
internal class PageInfo (selectionSet: SelectionSet) : FieldWithSelectionSet("pageInfo", selectionSet = selectionSet) {
    init {
        selectionSet += HasNextPageField()
        selectionSet += HasPreviousField()
    }
    inner class HasNextPageField : Field("hasNextPage")
    inner class HasPreviousField: Field("hasPrevious")
}

