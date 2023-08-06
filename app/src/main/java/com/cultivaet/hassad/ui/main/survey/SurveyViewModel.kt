package com.cultivaet.hassad.ui.main.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.domain.usecase.SurveyUseCase
import com.cultivaet.hassad.ui.main.farmers.FarmerDataItem
import com.cultivaet.hassad.ui.main.survey.intent.SurveyIntent
import com.cultivaet.hassad.ui.main.survey.viewstate.SurveyState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
class SurveyViewModel(
    private val surveyUseCase: SurveyUseCase
) : ViewModel() {
    val surveyIntent = Channel<SurveyIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<SurveyState>(SurveyState.Idle)
    val state: StateFlow<SurveyState> = _state
    var farmersList: List<FarmerDataItem>? = null
    internal var userId: Int = -1
    internal var farmerId: Int = -1

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            surveyIntent.consumeAsFlow().collect {
                when (it) {
                    is SurveyIntent.GetUserId -> getUserId()

                    is SurveyIntent.FetchAllFarmers -> getAllFarmersById(userId)

                    is SurveyIntent.FetchFarmerForm -> getFarmerForm(farmerId)
                }
            }
        }
    }

    private fun getUserId() {
        runBlocking {
            viewModelScope.launch {
                surveyUseCase.userId().collect { id ->
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
            _state.value = SurveyState.Loading
            _state.value =
                when (val resource = surveyUseCase.getAllFarmersById(id)) {
                    is Resource.Success -> {
                        farmersList = resource.data?.map { it.toFarmerDataItem() }
                        SurveyState.Success(farmersList)
                    }

                    is Resource.Error -> SurveyState.Error(resource.error)
                }
        }
    }

    private fun getFarmerForm(id: Int) {
        viewModelScope.launch {
            _state.value = SurveyState.Loading
            _state.value =
                when (val resource = surveyUseCase.getFarmerForm(id)) {
                    is Resource.Success -> {
                        val form = resource.data
                        SurveyState.Success(form)
                    }

                    is Resource.Error -> SurveyState.Error(resource.error)
                }
        }
    }
}