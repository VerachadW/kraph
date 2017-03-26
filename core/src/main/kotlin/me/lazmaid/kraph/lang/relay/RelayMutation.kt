package me.lazmaid.kraph.lang.relay

import me.lazmaid.kraph.lang.Field
import me.lazmaid.kraph.lang.SelectionSet

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class Mutation(name: String, arguments: InputArgument, selectionSet: SelectionSet) : Field(name, arguments, selectionSet)
