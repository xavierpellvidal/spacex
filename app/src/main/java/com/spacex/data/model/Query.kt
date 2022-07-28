package com.spacex.data.model

import java.io.Serializable

data class Query(

    var query: QueryObject

) : Serializable

data class QueryObject(

    var rocket: String

) : Serializable