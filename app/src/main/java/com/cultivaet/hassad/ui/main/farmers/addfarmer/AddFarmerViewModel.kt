package com.cultivaet.hassad.ui.main.farmers.addfarmer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.model.remote.requests.Farmer
import com.cultivaet.hassad.domain.usecase.AddFarmerUseCase
import com.cultivaet.hassad.ui.main.farmers.addfarmer.intent.AddFarmerIntent
import com.cultivaet.hassad.ui.main.farmers.addfarmer.viewstate.AddFarmerState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class AddFarmerViewModel(
    private val addFarmerUseCase: AddFarmerUseCase
) : ViewModel() {
    val addFarmerIntent = Channel<AddFarmerIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<AddFarmerState>(AddFarmerState.Idle)
    val state: StateFlow<AddFarmerState> = _state

    internal var userId: Int = -1

    internal lateinit var farmer: Farmer

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            addFarmerIntent.consumeAsFlow().collect {
                when (it) {
                    is AddFarmerIntent.AddFarmer -> addFarmer(farmer)

                    is AddFarmerIntent.InsertFarmerOffline -> insertFarmerOffline(farmer)
                }
            }
        }
    }

    private fun addFarmer(farmer: Farmer) {
        viewModelScope.launch {
            _state.value = AddFarmerState.Loading
            _state.value = when (val resource = addFarmerUseCase.addFarmer(farmer)) {
                is Resource.Success -> {
                    AddFarmerState.Success(resource.data)
                }

                is Resource.Error -> AddFarmerState.Error(resource.error)
            }
        }
    }

    private fun insertFarmerOffline(farmer: Farmer) {
        val farmerLocal = com.cultivaet.hassad.domain.model.local.Farmer(
            farmer.firstName,
            farmer.lastName,
            farmer.phoneNumber,
            farmer.gender,
            farmer.age,
            farmer.address,
            farmer.landArea,
            farmer.ownership,
            farmer.geolocation,
            farmer.ZeroDay,
            farmer.cropType,
            farmer.cropsHistory,
            farmer.facilitatorId,
        )

        viewModelScope.launch { addFarmerUseCase.insertFarmer(farmerLocal) }

        Log.d("TAG", "insertFarmerOffline: $farmerLocal")
    }
}