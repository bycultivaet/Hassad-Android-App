package com.cultivaet.hassad.ui.main.addfarmer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.requests.Farmer
import com.cultivaet.hassad.domain.usecase.AddFarmerUseCase
import com.cultivaet.hassad.ui.main.addfarmer.intent.AddFarmerIntent
import com.cultivaet.hassad.ui.main.addfarmer.viewstate.AddFarmerState
import com.cultivaet.hassad.ui.main.farmers.FarmerDataItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
class AddFarmerViewModel(
    private val addFarmerUseCase: AddFarmerUseCase
) : ViewModel() {
    val addFarmerIntent = Channel<AddFarmerIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<AddFarmerState>(AddFarmerState.Idle)
    val state: StateFlow<AddFarmerState> = _state
    internal var userId: Int = -1
    internal lateinit var farmer: Farmer
    var farmersList: List<FarmerDataItem>? = null

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            addFarmerIntent.consumeAsFlow().collect {
                when (it) {
                    is AddFarmerIntent.GetUserId -> getUserId()

                    is AddFarmerIntent.FetchAllFarmers -> getAllFarmersById(userId)

                    is AddFarmerIntent.AddFarmer -> addFarmer(farmer)
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
            _state.value = AddFarmerState.Loading
            _state.value =
                when (val resource = addFarmerUseCase.getAllFarmersById(id)) {
                    is Resource.Success -> {
                        farmersList = resource.data?.map { it.toFarmerDataItem() }
                        AddFarmerState.Success(farmersList)
                    }

                    is Resource.Error -> AddFarmerState.Error(resource.error)
                }
        }
    }

    private fun addFarmer(farmer: Farmer) {
        viewModelScope.launch {
            _state.value = AddFarmerState.Loading
            _state.value =
                when (val resource = addFarmerUseCase.addFarmer(farmer)) {
                    is Resource.Success -> {
                        AddFarmerState.Success(resource.data)
                    }

                    is Resource.Error -> AddFarmerState.Error(resource.error)
                }
        }
    }
}