package com.cultivaet.hassad.ui.main.farmers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.requests.Farmer
import com.cultivaet.hassad.domain.usecase.AddFarmerUseCase
import com.cultivaet.hassad.ui.main.farmers.intent.FarmersIntent
import com.cultivaet.hassad.ui.main.farmers.viewstate.FarmersState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
class FarmersViewModel(
    private val addFarmerUseCase: AddFarmerUseCase
) : ViewModel() {
    val farmersIntent = Channel<FarmersIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<FarmersState>(FarmersState.Idle)
    val state: StateFlow<FarmersState> = _state
    internal var userId: Int = -1
    internal lateinit var farmer: Farmer
    var farmersList: List<FarmerDataItem>? = null

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            farmersIntent.consumeAsFlow().collect {
                when (it) {
                    is FarmersIntent.GetUserId -> getUserId()

                    is FarmersIntent.FetchAllFarmers -> getAllFarmersById(userId)
                }
            }
        }
    }

    private fun getUserId() {
        runBlocking {
            viewModelScope.launch {
                addFarmerUseCase.userId().collect { id ->
                    if (id != null) {
                        userId = id
                        getAllFarmersById(userId)
                    }
                }
            }
        }
    }

    private fun getAllFarmersById(id: Int) {
        viewModelScope.launch {
            _state.value = FarmersState.Loading
            _state.value =
                when (val resource = addFarmerUseCase.getAllFarmersById(id)) {
                    is Resource.Success -> {
                        farmersList = resource.data?.map { it.toFarmerDataItem() }
                        FarmersState.Success(farmersList)
                    }

                    is Resource.Error -> FarmersState.Error(resource.error)
                }
        }
    }
}