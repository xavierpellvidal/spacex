package com.spacex.model.repository.api.v4

import com.spacex.SpaceXApp
import com.spacex.model.data.LaunchesQueryResponse
import com.spacex.model.data.Query
import com.spacex.model.data.Rocket

object SpaceXAPIRetriever {

    // Api functions

    /**
     * GET ROCKET BY ID
     */
    suspend fun getRocket(id: String): SpaceXAPINetworkResult<Rocket> {
        return handleApiResult { SpaceXApp.apiService.getApi().getRocket(id) }
    }

    /**
     * GET ALL ROCKETS
     */
    suspend fun getRocketsList(): SpaceXAPINetworkResult<MutableList<Rocket>> {
        return handleApiResult { SpaceXApp.apiService.getApi().getRocketsList() }
    }

    /**
     * GET ROCKET LAUNCHES
     */
    suspend fun getLaunches(query: Query): SpaceXAPINetworkResult<LaunchesQueryResponse> {
        return handleApiResult { SpaceXApp.apiService.getApi().getLaunches(query) }
    }
}