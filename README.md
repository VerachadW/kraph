# Kraph [![Build Status](https://travis-ci.org/VerachadW/kraph.svg?branch=master)](https://travis-ci.org/VerachadW/kraph) [ ![Download](https://api.bintray.com/packages/verachadw/maven/kraph/images/download.svg) ](https://bintray.com/tw/maven/kraph/_latestVersion) [![codecov](https://codecov.io/gh/VerachadW/kraph/branch/master/graph/badge.svg)](https://codecov.io/gh/VerachadW/kraph)

In short, this is a GraphQL request JSON body builder for Kotlin. It will
generate the JSON string for request body that work with GraphQL Server.
For example, we have this GraphQL query to list all notes:
```graphql
query {
    notes {
        id
        createdDate
        content
        author {
            name
            avatarUrl(size: 100)
        }
    }
}
```

Which is written in Kotlin using Kraph like this:
```kotlin
Kraph {
    query {
        fieldObject("notes") {
            field("id")
            field("createdDate")
            field("content")
            fieldObject("author") {
                field("name")
                field("avatarUrl", mapOf("size" to 100))
            }
        }
    }
}
```
As you can see, we can achieve our goal with just a few tweaks from the original query.

**NOTE:** Kraph is still in an early stage. The usage may change in further development.

### Features

-   DSL builder style. Make it easier to read and use.
-   Support Cursor Connection and Input Object Mutation in Relay.

### Set up

Adding Kraph to `build.gradle`
```gradle
repositories {
    jcenter()
}

dependencies {
    compile "me.lazmaid.kraph:kraph:x.y.z"
}
```

### Guide

If you are not familiar with GraphQL syntax, it is recommended to read the
[GraphQL introduction](http://graphql.org/learn/) for an overview of how Graphql
works. Usually, you should be able to use queries from other tools (e.g.
[GraphiQL](https://github.com/graphql/graphiql)) with a few tweaks.
First, let's see what Kraph provides for you.

#### Simple GraphQL

-   `query` and `mutation` represents the Query and Mutation operations of GraphQL.
    The name of the query or mutaton can be passed as a string.

    GraphQL:
    ```graphql
    query GetUsers {
      ...
    }
    ```
    Kraph:
    ```kotlin
    Kraph {
        query("GetUsers") {
            ...
        }
    }
    ```
    GraphQL:
    ```graphql
    mutation UpdateUserProfile {
      ...
    }
    ```
    Kraph:
    ```kotlin
    Kraph {
        mutation("UpdateUserProfile") {
            ...
        }
    }
    ```
-   `field` and `fieldObject` represent accessors for fields. Though there are
    technically no differences, `fieldObject` may be chosen for clarity to indicate
    that a field must contain another set of nested fields as an argument.
    Both of them take a `Map<String, Any>` that maps Kotlin data types to the
    GraphQL data types for input objects. You can also specify an alias using `alias` to change
    the name of the returned field.
    ```graphql
    query {
      users {
        nick: name
        email
        avatarUrl(size: 100)
      }
    }
    ```
    ```kotlin
    Kraph {
        query {
            fieldObject("users") {
                field("name", alias = "nick")
                field("email")
                field("avatarUrl", args = mapOf("size" to 100))
            }
        }
    }
    ```

-   `fragment` provides a mechanism for creating GraphQL Fragments. To use a fragment
    in a query requires two steps. The first is to define the fragment, letting
    Kraph know how to handle it later:
    ```graphql
    fragment UserFragment on User {
      name
      email
      avatarUrl(size: 100)
    }
    ```
    ```kotlin
    Kraph.defineFragment("UserFragment") {
        field("name")
        field("email")
        field("avatarUrl", mapOf("size" to 100))
    }
    ```
    Then, when you are creating your query, you can simply use the fragment and
    its fields will be expanded:
    ```graphql
    query {
      users {
        ...UserFragment
      }
    }
    ```

-   `fragment` provides a mechanism for creating GraphQL Fragments. To use a fragment
    in a query requires two steps. The first is to define the fragment, letting
    Kraph know how to handle it later:
    ```graphql
    fragment UserFragment on User {
      name
      email
      avatarUrl(size: 100)
    }
    ```
    ```kotlin
    Kraph.defineFragment("UserFragment") {
        field("name")
        field("email")
        field("avatarUrl", mapOf("size" to 100))
    }
    ```
    Then, when you are creating your query, you can simply use the fragment and
    its fields will be expanded:
    ```graphql
    query {
      users {
        ...UserFragment
      }
    }
    ```
    ```kotlin
    Kraph {
        query("GetUsers") {
            fieldObject("users") {
                fragment("UserFragment")
            }
        }
    }
    ```

#### Relay

-   `func` represents a Field inside a Mutation block that follows the
    [Relay Input Object Mutations](https://facebook.github.io/relay/graphql/mutations.htm) specification.
    ```graphql
    mutation {
      userLogin(input: {email: "hello@taskworld.com", password: "abcd1234"}) {
        accessToken
        user {
          id
          email
        }
      }
    }
    ```
    ```kotlin
    Kraph {
        mutation {
            func("userLogin", input = mapOf("email" to "hello@taskworld.com", "password" to "abcd1234")) {
                field("accessToken")
                fieldObject("user") {
                    field("id")
                    field("email")
                }
            }
        }
    }
    ```
-   `cursorConnection` represents a Field that follows the
    [Relay Cursor Connections](https://facebook.github.io/relay/graphql/connections.htm) specification
    ```graphql
    query {
       users(first: 10, after: "user::1234") {
        edges {
          node {
            id
            name
          }
        }
      }
    }
    ```
    ```kotlin
    Kraph {
        cursorConnection("users", first = 10, after = "user::1234") {   
            edges {
                node {
                    field("id")
                    field("name")
                }
            }
        }
    }
    ```

#### Request/Query String

-   `toRequestString()` will generate a JSON body to send in POST request.
-   `toGraphQueryString()` will give you the formatted GraphQL string. This is
    very useful for debugging.
    ```kotlin
    val query = Kraph {
        query {
            fieldObject("users") {
                field("name")
                field("email")
                field("avatarUrl", args = mapOf("size" to 100))
            }
        }
    }    

    println(query.toRequestString())
    /*
     * Result:
     * {"query": "query {\nnotes {\nid\ncreatedDate\ncontent\nauthor {\nname\navatarUrl(size: 100)\n}\n}\n}", "variables": null, "operationName": null}
     */
    println(query.toGraphQueryString())
    /*
     * Result:
     * query {
     *   notes {
     *     id
     *     createdDate
     *     content
     *     author {
     *       name
     *       avatarUrl(size: 100)
     *     }
     *   }
     * }
     */
    ```
-   `requestQueryString()`, `requestVariableString()` and `requestOperationName()`
    provide more fine grained access to the components of the full request string,
    which are sometimes necessary depending on your HTTP request builder and
    GraphQL server setup. They provide the values for the `query`, `variables`,
    and `operationName` parameters, respectively, and so are good for creating
    GET requests. Please note that `requestVariableString()` will always return
    `null` until variable support is implemented.

### Contributing to Kraph

We use Github issues for tracking bugs and requests.
Any feedback and/or PRs is welcome.
