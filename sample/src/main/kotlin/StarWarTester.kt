import com.github.kittinunf.fuel.httpPost
import com.taskworld.kraph.KraphQL

/**
 * Created by VerachadW on 9/27/2016 AD.
 */

fun main(args: Array<String>) {
    val url = "http://graphql-swapi.parseapp.com/?"

    val query =  KraphQL {
        query {
            fieldObject("allFilms", mapOf("first" to 10)) {
                fieldObject("films") {
                    field("title")
                }
            }
        }
    }.toString()

    print(query)
    url.httpPost().header("content-type" to "application/json", "Accept" to "application/json").body(query).responseString { request, response, result ->
        print(response)
    }

}