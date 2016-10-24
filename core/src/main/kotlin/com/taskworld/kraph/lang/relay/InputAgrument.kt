package com.taskworld.kraph.lang.relay

import com.taskworld.kraph.lang.Argument

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class InputArgument(args: Map<String, Any>) : Argument(args) {
    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        return "(input: { ${args.print(prettyFormat)} })"
    }
}
