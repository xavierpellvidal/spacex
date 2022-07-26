package com.spacex.model.providers

import com.spacex.model.data.Query
import com.spacex.model.providers.responses.LaunchesResponse
import com.spacex.model.repository.api.v4.SpaceXAPINetworkResult
import com.spacex.model.repository.api.v4.SpaceXAPIRetriever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LaunchesProvider {
    suspend fun getLaunches(query: Query): LaunchesResponse<Any> {
        return withContext(Dispatchers.IO) {
            when (val result = SpaceXAPIRetriever.getLaunches(query)) {
                is SpaceXAPINetworkResult.Success -> {
                    if (result.data.docs.size == 0) LaunchesResponse.NoLaunches
                    else LaunchesResponse.Success(result.data.docs)
                }
                is SpaceXAPINetworkResult.ErrorBodyNull -> LaunchesResponse.NoLaunches
                is SpaceXAPINetworkResult.NoNetwork -> LaunchesResponse.NoNetwork
                is SpaceXAPINetworkResult.Error -> LaunchesResponse.GenericError
                is SpaceXAPINetworkResult.Exception -> LaunchesResponse.GenericError
            }
        }
    }
}