package com.taskworld.kraph.lang.relay

import com.taskworld.kraph.lang.Argument
import com.taskworld.kraph.lang.print

/**
 * Created by VerachadW on 10/2/2016 AD.
 */

internal class InputArgument(args: Map<String, Any>) : Argument(args) {
    override fun print(): String {
        return "(input: { ${args.print()} })"
    }
}
