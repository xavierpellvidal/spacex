package com.spacex.domain

sealed class UseCaseResponse<out T : Any> {
    class Success<T : Any>(val data: T) : UseCaseResponse<T>()
    object NoData : UseCaseResponse<Nothing>()
    object NoNetwork : UseCaseResponse<Nothing>()
    object GenericError : UseCaseResponse<Nothing>()
}
