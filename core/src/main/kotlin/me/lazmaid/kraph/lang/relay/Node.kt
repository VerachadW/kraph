package me.lazmaid.kraph.lang.relay

import me.lazmaid.kraph.lang.Field
import me.lazmaid.kraph.lang.SelectionSet


/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class Node(name: String, additionalFields: List<Field>) : Field(name) {
    init {
        selectionSet = SelectionSet(additionalFields.toMutableList().apply {
           add(0, Field("id"))
        })
    }
}