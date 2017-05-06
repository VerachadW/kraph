package me.lazmaid.kraph.lang.relay

import me.lazmaid.kraph.lang.FieldWithSelectionSet
import me.lazmaid.kraph.lang.SelectionSet

internal class Mutation(name: String, arguments: InputArgument, selectionSet: SelectionSet) : FieldWithSelectionSet(name, arguments, selectionSet)
