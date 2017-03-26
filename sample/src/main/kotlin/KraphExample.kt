import me.lazmaid.kraph.Kraph

fun main(args: Array<String>) {

    // Sample query
    val query = Kraph {
        query {
            fieldObject("notes") {
                field("id")
                field("createdDate")
                field("content")
                fieldObject("author") {
                    field("name")
                    field("avatarUrl", args = mapOf("size" to 100))
                }
            }
        }
    }

    println("Query: ${query.toGraphQueryString()}")
    println("Request Body: ${ query.toRequestString() }")

    // Sample mutation
    val mutation = Kraph {
        mutation {
            func("updateNote", args = mapOf("id" to 123, "title" to "Hello Kraph")) {
                field("id")
                field("title")
                field("updatedDate")
            }
        }
    }

    println("Query: ${mutation.toGraphQueryString()}")
    println("Request Body: ${ mutation.toRequestString() }")


}