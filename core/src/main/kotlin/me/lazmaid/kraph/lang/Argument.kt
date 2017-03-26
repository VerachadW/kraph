package me.lazmaid.kraph.lang

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal open class Argument(internal val args: Map<String, Any> = mapOf()) : GraphQLNode() {
    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        return "(${args.print(prettyFormat)})"
    }
}