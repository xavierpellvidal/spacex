package com.spacex.domain.launch

import com.spacex.data.model.Launch
import com.spacex.data.model.Query
import com.spacex.data.repository.LaunchesRepository
import com.spacex.domain.UseCaseResponse
import com.spacex.data.repository.RepositoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLaunches {
    suspend fun getLaunches(query: Query): UseCaseResponse<MutableList<Launch>> {
        return withContext(Dispatchers.IO) {
            when (val result = LaunchesRepository.getLaunches(query)) {
                is RepositoryResponse.Success -> {
                    if (result.data.docs.size == 0) UseCaseResponse.NoData
                    else UseCaseResponse.Success(result.data.docs)
                }
                is RepositoryResponse.NoData -> UseCaseResponse.NoData
                is RepositoryResponse.NoNetwork -> UseCaseResponse.NoNetwork
                is RepositoryResponse.GenericError -> UseCaseResponse.GenericError
            }
        }
    }
}