package com.spacex.model.data

import java.io.Serializable
import java.util.*

class Launch(

    var name: String,
    var date_utc: Date,
    var success: Boolean,
    var links: Links

) : Serializable

class Links(

    var presskit: String?,
    var youtube_id: String?,
    var wikipedia: String?

) : Serializable

class LaunchesQueryResponse(

    var docs: MutableList<Launch>

) : Serializable


