package com.spacex.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.data.model.Launch
import com.spacex.data.model.Query
import com.spacex.data.model.QueryObject
import com.spacex.data.model.Rocket
import com.spacex.UseCaseResponse
import com.spacex.ui.main.domain.GetLaunches
import com.spacex.ui.main.domain.GetRocket
import com.spacex.ViewModelResponse
import kotlinx.coroutines.launch

class RocketViewModel : ViewModel() {
    // Domain use cases
    private var rocketUseCase = GetRocket()
    private var launchesUseCase = GetLaunches()

    // Livedata
    private val _rocket: MutableLiveData<Rocket> = MutableLiveData()
    val rocket: LiveData<Rocket> = _rocket
    private val _launches: MutableLiveData<MutableList<Launch>> = MutableLiveData()
    val launches: LiveData<MutableList<Launch>> = _launches
    private val _loader: MutableLiveData<Boolean> = MutableLiveData()
    val loader: LiveData<Boolean> = _loader
    private val _rocketError: MutableLiveData<ViewModelResponse> = MutableLiveData()
    val rocketError: LiveData<ViewModelResponse> = _rocketError
    private val _launchesError: MutableLiveData<ViewModelResponse> = MutableLiveData()
    val launchesError: LiveData<ViewModelResponse> = _launchesError

    // View Model
    fun loadRocket(id: String) {
        /* Load rocket coroutine */
        viewModelScope.launch {
            // Start data loading
            _loader.postValue(true)

            //Load rocket info
            loadData(id)

            // End data loading
            _loader.postValue(false)
        }
    }

    private suspend fun loadData(id: String) {
        // Load rocket info
        when (val response = rocketUseCase.getRocket(id)) {
            is UseCaseResponse.Success -> {
                // Post value to view
                _rocket.postValue(response.data)
            }
            is UseCaseResponse.NoData -> _rocketError.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> _rocketError.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> _rocketError.postValue(ViewModelResponse.GENERIC_ERROR)
        }

        // Load rocket launches with Query
        when (val response = launchesUseCase.getLaunches(Query(QueryObject(rocket = id)))) {
            is UseCaseResponse.Success -> {
                // Post value to view
                _launches.postValue(response.data)
            }
            is UseCaseResponse.NoData -> _launchesError.postValue(ViewModelResponse.NULL_EMPTY_DATA)
            is UseCaseResponse.NoNetwork -> _launchesError.postValue(ViewModelResponse.NO_NETWORK)
            is UseCaseResponse.GenericError -> _launchesError.postValue(ViewModelResponse.GENERIC_ERROR)
        }
    }
}