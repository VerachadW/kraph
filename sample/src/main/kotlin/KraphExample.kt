import com.taskworld.kraph.Kraph

fun main(args: Array<String>) {

    // Sample query
    println(Kraph {
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
    })

    // Sample mutation
    println(Kraph {
        mutation {
            func("updateNote", args = mapOf("id" to 123, "title" to "Hello Kraph")) {
                field("id")
                field("title")
                field("updatedDate")
            }
        }
    })

}