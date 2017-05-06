package me.lazmaid.kraph.lang.relay

import me.lazmaid.kraph.lang.Argument

internal class InputArgument(args: Map<String, Any>) : Argument(args) {
    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        return "(input: { ${args.print(prettyFormat)} })"
    }
}
