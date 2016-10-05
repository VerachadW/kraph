package com.taskworld.kraph.lang.relay

import com.taskworld.kraph.lang.Field
import com.taskworld.kraph.lang.SelectionSet

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class Mutation(name: String, arguments: InputArgument, selectionSet: SelectionSet) : Field(name, arguments, selectionSet)
