package com.spacex.data.provider.api

import retrofit2.Response
import java.net.UnknownHostException

sealed class SpaceXAPINetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : SpaceXAPINetworkResult<T>()
    class BodyNull<T : Any>(val data: T?) : SpaceXAPINetworkResult<T>()
    class NoNetwork<T : Any> : SpaceXAPINetworkResult<T>()
    class Error<T : Any>(val code: Int) : SpaceXAPINetworkResult<T>()
    class Exception<T : Any> : SpaceXAPINetworkResult<T>()
}

suspend fun <T : Any> handleApiResult(execute: suspend () -> Response<T>): SpaceXAPINetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful) {
            if (body != null) SpaceXAPINetworkResult.Success(body)
            else SpaceXAPINetworkResult.BodyNull(body)
        } else {
            SpaceXAPINetworkResult.Error(code = response.code())
        }
    } catch (e: UnknownHostException) {
        return SpaceXAPINetworkResult.NoNetwork()
    } catch (e: Exception) {
        return SpaceXAPINetworkResult.Exception()
    }
}