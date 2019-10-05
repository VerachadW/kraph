package me.lazmaid.kraph.test

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import me.lazmaid.kraph.Kraph
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

class RequestSpek : Spek({
    describe("Kraph Query Request format printers") {
        val query = Kraph {
            query("GetUserId") {
                field("user", args = mapOf("name" to variable("name", "User", "{\"name\": \"UserName\"}"))) {
                    field("id")
                }
            }
        }
        describe("#toRequestString") {
            given("document with simple query") {
                it("should print the entire document") {
                    assertThat(query.toRequestString(), equalTo("{\"query\": \"query GetUserId (\$name: User) { user (name: \$name) { id } }\", \"variables\": {\"name\": {\"name\": \"UserName\"}}, \"operationName\": \"GetUserId\"}"))
                }
            }
        }
        describe("#requestQueryString") {
            it("should print just the query portion of the document") {
                assertThat(query.requestQueryString(), equalTo("query GetUserId (\$name: User) { user (name: \$name) { id } }"))
            }
        }
        describe("#requestVariableString") {
            assertThat(query.requestVariableString(), equalTo("{\"name\": {\"name\": \"UserName\"}}"))
        }
        describe("#requestOperationName") {
            given("document with simple query") {
                it("should print the name of the query") {
                    assertThat(query.requestOperationName(), equalTo("GetUserId"))
                }
            }
        }
    }
})
