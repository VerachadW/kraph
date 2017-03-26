import com.github.kittinunf.fuel.httpPost
import me.lazmaid.kraph.Kraph

fun main(args: Array<String>) {
    val url = "http://graphql-swapi.parseapp.com/?"

    val query = Kraph {
        query {
            cursorConnection("allFilms", first = 10) {
                edges {
                    node {
                        field("title")
                    }
                }
            }
        }
    }

    url.httpPost().header("content-type" to "application/json", "Accept" to "application/json").body(query.toRequestString()).responseString { request, response, result ->
        println(response)
    }
}