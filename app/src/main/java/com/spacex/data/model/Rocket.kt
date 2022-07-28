package com.spacex.data.model

import java.io.Serializable

data class Rocket(

    var id: String,
    var name: String,
    var active: Boolean,
    var description: String,
    var country: String,
    var engines: Engines,
    var height: Height,
    var mass: Mass,
    var flickr_images: MutableList<String>,

    ) : Serializable

data class Engines(

    var number: Int,

    ) : Serializable

data class Height(

    var meters: Double,

    ) : Serializable

data class Mass(

    var kg: Int

) : Serializable