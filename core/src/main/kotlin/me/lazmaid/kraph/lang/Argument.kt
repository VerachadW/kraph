package me.lazmaid.kraph.lang

open class Argument(internal val args: Map<String, Any> = mapOf()) : GraphQLNode() {
    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        return "(${args.print(prettyFormat)})"
    }
}