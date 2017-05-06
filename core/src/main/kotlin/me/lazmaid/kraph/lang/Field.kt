package me.lazmaid.kraph.lang

open class Field(val name: String, val arguments: Argument? = null) : GraphQLNode() {
    override fun print(prettyFormat: Boolean, previousLevel: Int): String {
        return "$name${arguments?.print(prettyFormat, previousLevel) ?: ""}"
    }
}




