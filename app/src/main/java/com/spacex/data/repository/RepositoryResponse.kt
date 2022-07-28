package com.spacex.data.repository

sealed class RepositoryResponse<out T : Any>{

    class Success<T: Any>(val data: T) : RepositoryResponse<T>()
    object NoData : RepositoryResponse<Nothing>()
    object NoNetwork : RepositoryResponse<Nothing>()
    object GenericError : RepositoryResponse<Nothing>()

}