package com.spacex.domain.rocket

import com.spacex.data.model.Rocket
import com.spacex.data.repository.RepositoryResponse
import com.spacex.data.repository.RocketsRepository
import com.spacex.domain.UseCaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetRocket {
    suspend fun getRocket(id: String): UseCaseResponse<Rocket> {
        return withContext(Dispatchers.IO) {
            when (val result = RocketsRepository.getRocket(id)) {
                is RepositoryResponse.Success -> UseCaseResponse.Success(result.data)
                is RepositoryResponse.NoData -> UseCaseResponse.NoData
                is RepositoryResponse.NoNetwork -> UseCaseResponse.NoNetwork
                is RepositoryResponse.GenericError -> UseCaseResponse.GenericError
            }
        }
    }
}