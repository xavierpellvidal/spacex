package com.spacex.model.data

import java.io.Serializable

class Rocket(

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

class Engines(

    var number: Int,

    ) : Serializable

class Height(

    var meters: Double,

    ) : Serializable

class Mass(

    var kg: Int

) : Serializable