package me.lazmaid.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal open class Argument(internal val args: Map<String, Any> = mapOf()) : GraphQLNode() {
    override fun print(
        format: PrintFormat,
        previousLevel: Int
    ): String {
        return "(${print(args, format)})"
    }
}
