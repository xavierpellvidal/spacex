package com.spacex.model.providers.responses

import com.spacex.model.data.Launch

sealed class LaunchesResponse<out T : Any> {
    class Success(val data: MutableList<Launch>) : LaunchesResponse<MutableList<Launch>>()
    object NoLaunches : LaunchesResponse<Nothing>()
    object NoNetwork : LaunchesResponse<Nothing>()
    object GenericError : LaunchesResponse<Nothing>()
}
