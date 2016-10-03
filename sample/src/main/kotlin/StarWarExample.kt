import com.github.kittinunf.fuel.httpPost
import com.taskworld.kraph.Kraph

fun main(args: Array<String>) {
    val url = "http://graphql-swapi.parseapp.com/?"

    val query = Kraph {
        query {
            connection("allFilms", first = 10) {
                edges {
                    node {
                        field("title")
                    }
                }
            }
        }
    }

    println(query)

    url.httpPost().header("content-type" to "application/json", "Accept" to "application/json").body(query.toString()).responseString { request, response, result ->
        println(response)
    }
}