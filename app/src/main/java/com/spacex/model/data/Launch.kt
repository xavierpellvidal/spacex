package com.spacex.model.data

import java.io.Serializable
import java.util.*

data class Launch(

    var name: String,
    var date_utc: Date,
    var success: Boolean,
    var links: Links

) : Serializable

data class Links(

    var presskit: String?,
    var youtube_id: String?,
    var wikipedia: String?

) : Serializable

data class LaunchesQueryResponse(

    var docs: MutableList<Launch>

) : Serializable


