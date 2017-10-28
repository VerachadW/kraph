package me.lazmaid.kraph.test

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import me.lazmaid.kraph.Kraph
import me.lazmaid.kraph.NoFieldsInSelectionSetException
import me.lazmaid.kraph.NoSuchFragmentException
import me.lazmaid.kraph.lang.OperationType
import me.lazmaid.kraph.lang.relay.CursorConnection
import me.lazmaid.kraph.lang.relay.PageInfo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

class RequestSpek : Spek({
    describe("Kraph Query Request format printers") {
        val query = Kraph {
            query("GetUserId") {
                field("user", mapOf("name" to "UserName")) {
                    field("id")
                }
            }
        }
        describe("#toRequestString") {
            given("document with simple query") {
                it("should print the entire document") {
                    assertThat(query.toRequestString(), equalTo("{\"query\": \"query GetUserId { user (name: \\\"UserName\\\") { id } }\", \"variables\": null, \"operationName\": \"GetUserId\"}"))
                }
            }
        }
        describe("#requestQueryString") {
            it("should print just the query portion of the document") {
                assertThat(query.requestQueryString(), equalTo("query GetUserId { user (name: \"UserName\") { id } }"))
            }
        }
        describe("#requestVariableString") {
            // TODO: variables not yet implemented
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
