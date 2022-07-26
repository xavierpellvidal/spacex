package com.spacex.model.providers

import com.spacex.SpaceXApp
import com.spacex.model.data.Rocket
import com.spacex.model.providers.responses.RocketResponse
import com.spacex.model.providers.responses.RocketsListResponse
import com.spacex.model.repository.api.v4.SpaceXAPINetworkResult
import com.spacex.model.repository.api.v4.SpaceXAPIRetriever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RocketsProvider {
    suspend fun getRockets(): RocketsListResponse<Any> {
        return withContext(Dispatchers.IO) {
            when (val result = SpaceXAPIRetriever.getRocketsList()) {
                is SpaceXAPINetworkResult.Success -> {
                    if (result.data.size > 0){
                        var rockets = result.data

                        // If rockets need to be filtered
                        if (SpaceXApp.mPrefs.isRocketsFilterEnabled()) {
                            var filteredRockets: List<Rocket>
                            filteredRockets = result.data.filter {
                                it.active == SpaceXApp.mPrefs.getActiveFilter()
                            }
                            rockets = filteredRockets.toMutableList()
                        }

                        //Return
                        if(rockets.size > 0) RocketsListResponse.Success(rockets)
                        else RocketsListResponse.NoRockets
                    } else RocketsListResponse.NoRockets
                }
                is SpaceXAPINetworkResult.ErrorBodyNull -> RocketsListResponse.NoRockets
                is SpaceXAPINetworkResult.NoNetwork -> RocketsListResponse.NoNetwork
                is SpaceXAPINetworkResult.Error -> RocketsListResponse.GenericError
                is SpaceXAPINetworkResult.Exception -> RocketsListResponse.GenericError
            }
        }
    }

    suspend fun getRocket(id: String): RocketResponse<Any> {
        return withContext(Dispatchers.IO) {
            when (val result = SpaceXAPIRetriever.getRocket(id)) {
                is SpaceXAPINetworkResult.Success -> RocketResponse.Success(result.data)
                is SpaceXAPINetworkResult.ErrorBodyNull -> RocketResponse.NoRocket
                is SpaceXAPINetworkResult.NoNetwork -> RocketResponse.NoNetwork
                is SpaceXAPINetworkResult.Error -> RocketResponse.GenericError
                is SpaceXAPINetworkResult.Exception -> RocketResponse.GenericError
            }
        }
    }
}