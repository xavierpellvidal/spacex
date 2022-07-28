package com.spacex.data.repository;

import com.spacex.SpaceXApp
import com.spacex.SpaceXApp.Companion.apiService
import com.spacex.data.model.LaunchesQueryResponse
import com.spacex.data.model.Query
import com.spacex.data.repository.api.v4.SpaceXAPINetworkResult
import com.spacex.data.repository.api.v4.handleApiResult

object LaunchesRepository {

        /**
         * GET ROCKET LAUNCHES
         */
        suspend fun getLaunches(query: Query): RepositoryResponse<LaunchesQueryResponse> {
                //We only have remote data source by the moment
                return when (val result = handleApiResult { apiService.getApi().getLaunches(query) }) {
                        is SpaceXAPINetworkResult.Success -> RepositoryResponse.Success(result.data)
                        is SpaceXAPINetworkResult.BodyNull -> RepositoryResponse.NoData
                        is SpaceXAPINetworkResult.NoNetwork -> RepositoryResponse.NoNetwork
                        is SpaceXAPINetworkResult.Error -> RepositoryResponse.GenericError
                        is SpaceXAPINetworkResult.Exception -> RepositoryResponse.GenericError
                }
        }

}
