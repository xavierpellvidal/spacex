package com.spacex.data.repository;

import com.spacex.SpaceXApp.Companion.apiService
import com.spacex.data.model.Rocket
import com.spacex.data.provider.api.SpaceXAPINetworkResult
import com.spacex.data.provider.api.handleApiResult

object RocketsRepository {

        /**
         * GET ROCKET BY ID
         */
        suspend fun getRocket(id: String): RepositoryResponse<Rocket> {
                //We only have remote data source by the moment
                return when (val result = handleApiResult { apiService.getApi().getRocket(id) }) {
                        is SpaceXAPINetworkResult.Success -> RepositoryResponse.Success(result.data)
                        is SpaceXAPINetworkResult.BodyNull -> RepositoryResponse.NoData
                        is SpaceXAPINetworkResult.NoNetwork -> RepositoryResponse.NoNetwork
                        is SpaceXAPINetworkResult.Error -> RepositoryResponse.GenericError
                        is SpaceXAPINetworkResult.Exception -> RepositoryResponse.GenericError
                }
        }

        /**
         * GET ALL ROCKETS
         */
        suspend fun getRocketsList(): RepositoryResponse<MutableList<Rocket>> {
                //We only have remote data source by the moment
                return when (val result = handleApiResult { apiService.getApi().getRocketsList() }) {
                        is SpaceXAPINetworkResult.Success -> RepositoryResponse.Success(result.data)
                        is SpaceXAPINetworkResult.BodyNull -> RepositoryResponse.NoData
                        is SpaceXAPINetworkResult.NoNetwork -> RepositoryResponse.NoNetwork
                        is SpaceXAPINetworkResult.Error -> RepositoryResponse.GenericError
                        is SpaceXAPINetworkResult.Exception -> RepositoryResponse.GenericError
                }
        }

}
