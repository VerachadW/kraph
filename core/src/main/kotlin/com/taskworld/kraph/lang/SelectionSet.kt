package com.taskworld.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class SelectionSet(internal val fields: List<Field>) : GraphPrintable {
    override fun print(): String {
        return "{\\n${fields.print()}}"
    }
}