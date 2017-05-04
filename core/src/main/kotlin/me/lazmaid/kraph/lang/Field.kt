package me.lazmaid.kraph.lang

/**
 * Created by VerachadW on 9/19/2016 AD.
 */

internal open class Field(val name: String, val arguments: Argument? = null) : GraphQLNode() {
    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        return "$name${arguments?.print(prettyFormat, previousLevel) ?: ""}"
    }
}




