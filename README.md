# Kraph [![Build Status](https://travis-ci.org/VerachadW/kraph.svg?branch=master)](https://travis-ci.org/VerachadW/kraph) [ ![Download](https://api.bintray.com/packages/tw/maven/kraph/images/download.svg) ](https://bintray.com/tw/maven/kraph/_latestVersion)
In short, this is a GraphQL request JSON body builder for Kotlin. It will generate the JSON string for request body that work with GraphQL Server. For example, we have the GraphQL query for list all the notes.
```
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
And here is how we write it in Kotlin using Kraph.
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
- DSL builder style. Make it easier to read and use.
- Support Cursor Connection and Input object Mutation in Relay.

### Set up
Adding Kraph to `build.gradle`
```kotlin
    repositories {
        jcenter()
    }
    
    dependencies {
        compile "com.taskworld.kraph:kraph:x.y.z"
    }
```

### Guide
If you are not familiar with GraphQL syntax, We recommended to read on this [specification](https://facebook.github.io/graphql/) to have an overview of how to write Graphql. Usually, you should be able to use the query from other tools(e.g. [GraphiQL](https://github.com/graphql/graphiql)) with a few tweaks. First, let's see what Kraph provided for you.
#### Simple GraphQL
- `query`/`mutation` represents QUERY and MUTATION operation in GraphQL. It can be named by passing String as a parameter. 
```kotlin
    /*
    * query GetUsers {
    *   ...
    * }
    */
    
    Kraph {
        query("GetUsers") {
            ...
        }
    }
    
    /*
    * mutation updateUserProfile {
    *   ...
    * }
    */
    
    Kraph {
        mutation("UpdateUserProfile") {
            ... 
        }
    }
```
- `field` and `fieldObject` represents FIELD in SELECTION SET. The different is that `fieldObject` allow you to have it owns SELECTION SET, which represent data object in GraphQL. Both of them have a parameter named `args` for arguments in paritcular field.
```kotlin
    /*
    * query {
    *   users {
    *       name
    *       email
    *       avatarUrl(size: 100)
    *   }
    * }
    */
    
    Kraph {
        query {
            fieldObject("users") {
                field("name")
                field("email")
                field("avatarUrl", args = mapOf("size" to 100))
            }
        }
    }
```
#### Relay
- `func` represents as FIELD inside MUTATION block that follow [Relay Input Object Mutations](https://facebook.github.io/relay/graphql/mutations.htm) specification.
```kotlin
    /*
    * mutation {
    *   userLogin(input: {email: "hello@taskworld.com", password: "abcd1234"}) {
    *       accessToken
    *       user {
    *           id
    *           email
    *       }
    *   }
    * }
    */
    
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
- `cursorConnection` represents as FIELD that follow [Relay Cursor Connections](https://facebook.github.io/relay/graphql/connections.htm) specification
```kotlin
    /*
    * query {
    *   users(first: 10, after: "user::1234") {
    *       edges {
    *           node {
    *               id
    *               name
    *           }
    *       }
    *   }
    * }
    */
    
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
- `toRequestString()` is used for generating request JSON body for POST method.
- `toGraphQueryString()` will give you the formatted GraphQuery string. This is very useful for debugging.
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
    * Result: {"query": "query {\nnotes {\nid\ncreatedDate\ncontent\nauthor {\nname\navatarUrl(size: 100)\n}\n}\n}", "variables": null, "operationName": null}
    */
    println(query.toGrapQueryString())
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
### Contributing to Kraph
We use Github issues for tracking bugs and requests. Any feedback and/or PRs is welcome.