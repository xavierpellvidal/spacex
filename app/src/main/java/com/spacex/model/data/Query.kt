package com.spacex.model.data

import java.io.Serializable

class Query(

    var query: QueryObject

) : Serializable

class QueryObject(

    var rocket: String

) : Serializable