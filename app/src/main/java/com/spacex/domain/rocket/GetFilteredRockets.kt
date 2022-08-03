package com.spacex.domain.rocket

import com.spacex.data.model.Rocket
import com.spacex.data.repository.RepositoryResponse
import com.spacex.data.repository.RocketsRepository
import com.spacex.domain.UseCaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFilteredRockets {
    suspend fun getFilteredRockets(active: Boolean): UseCaseResponse<MutableList<Rocket>> {
        return withContext(Dispatchers.IO) {
            when (val result = RocketsRepository.getRocketsList()) {
                is RepositoryResponse.Success -> {
                    if (result.data.size > 0){
                        var filteredRockets: List<Rocket>
                        filteredRockets = result.data.filter {
                            it.active == active
                        }
                        val rockets = filteredRockets.toMutableList()

                        //Return
                        if(rockets.size > 0) UseCaseResponse.Success(rockets)
                        else UseCaseResponse.NoData
                    } else UseCaseResponse.NoData
                }
                is RepositoryResponse.NoData -> UseCaseResponse.NoData
                is RepositoryResponse.NoNetwork -> UseCaseResponse.NoNetwork
                is RepositoryResponse.GenericError -> UseCaseResponse.GenericError
            }
        }
    }
}