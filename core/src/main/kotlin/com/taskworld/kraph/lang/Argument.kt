package com.taskworld.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal open class Argument(internal val args: Map<String, Any> = mapOf()) : GraphPrintable {
    override fun print(): String {
        return "(${args.print()})"
    }
}