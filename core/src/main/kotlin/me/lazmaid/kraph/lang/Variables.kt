package me.lazmaid.kraph.lang

import me.lazmaid.kraph.types.KraphVariable

/**
 * Created by OinkIguana on 10/25/2017 AD.
 */

internal class Variables : GraphQLNode() {

    val variables = mutableMapOf<String, KraphVariable>()

    override fun print(format: PrintFormat, previousLevel: Int): String =
    //format doesn't apply since the json value is passed by the library user
            variables.entries.joinToString(separator = ",", prefix = "{", postfix = "}") { "\"${it.key}\": ${it.value.jsonValue}" }

    fun isEmpty(): Boolean = variables.isEmpty()

    fun asArgument(): Argument? =
            variables.takeIf { it.isNotEmpty() }
                    ?.let { Argument(it.values.map { it.dollarName to it.type }.toMap()) }
}

