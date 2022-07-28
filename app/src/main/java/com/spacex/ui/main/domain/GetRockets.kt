package com.spacex.ui.main.domain

import com.spacex.data.model.Rocket
import com.spacex.data.repository.RepositoryResponse
import com.spacex.data.repository.RocketsRepository
import com.spacex.UseCaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetRockets {
    suspend fun getRockets(): UseCaseResponse<MutableList<Rocket>> {
        return withContext(Dispatchers.IO) {
            when (val result = RocketsRepository.getRocketsList()) {
                is RepositoryResponse.Success -> {
                    if (result.data.size > 0) UseCaseResponse.Success(result.data)
                    else UseCaseResponse.NoData
                }
                is RepositoryResponse.NoData -> UseCaseResponse.NoData
                is RepositoryResponse.NoNetwork -> UseCaseResponse.NoNetwork
                is RepositoryResponse.GenericError -> UseCaseResponse.GenericError
            }
        }
    }
}