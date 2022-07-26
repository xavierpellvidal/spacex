package com.spacex.model.providers.responses

import com.spacex.model.data.Rocket

sealed class RocketResponse<out T : Any> {
    class Success(val data: Rocket) : RocketResponse<Rocket>()
    object NoRocket : RocketResponse<Nothing>()
    object NoNetwork : RocketResponse<Nothing>()
    object GenericError : RocketResponse<Nothing>()
}

sealed class RocketsListResponse<out T : Any> {
    class Success(val data: MutableList<Rocket>) : RocketsListResponse<MutableList<Rocket>>()
    object NoRockets : RocketsListResponse<Nothing>()
    object NoNetwork : RocketsListResponse<Nothing>()
    object GenericError : RocketsListResponse<Nothing>()
}
