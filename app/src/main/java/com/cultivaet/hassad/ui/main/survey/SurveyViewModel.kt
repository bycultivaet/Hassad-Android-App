package com.cultivaet.hassad.ui.main.survey

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.core.extension.createTempImageFile
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.core.util.Utils
import com.cultivaet.hassad.domain.model.remote.requests.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.responses.Form
import com.cultivaet.hassad.domain.usecase.SurveyUseCase
import com.cultivaet.hassad.ui.main.farmers.FarmerDataItem
import com.cultivaet.hassad.ui.main.survey.intent.SurveyIntent
import com.cultivaet.hassad.ui.main.survey.viewstate.SurveyState
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@ExperimentalCoroutinesApi
class SurveyViewModel(
    private val application: Application,
    private val surveyUseCase: SurveyUseCase
) : ViewModel() {
    val surveyIntent = Channel<SurveyIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<SurveyState>(SurveyState.Idle)
    internal val state: StateFlow<SurveyState> = _state
    internal var farmersList: List<FarmerDataItem>? = null
    internal val facilitatorAnswer = FacilitatorAnswer()
    internal val facilitatorForm = Form()
    internal var uuidImages: String = ""
    internal var indexOfImages: Int = -1

    init {
        handleIntent()
        getUserId()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            surveyIntent.consumeAsFlow().collect {
                when (it) {
                    is SurveyIntent.GetUserId -> getUserId()

                    is SurveyIntent.FetchAllFarmers -> getAllFarmersById(facilitatorAnswer.userId)

                    is SurveyIntent.FetchFacilitatorForm -> getFacilitatorForm(facilitatorAnswer.userId)

                    is SurveyIntent.SubmitFacilitatorAnswer -> submitFacilitatorAnswer(
                        facilitatorAnswer
                    )

                    is SurveyIntent.InsertFacilitatorAnswerOffline -> insertFacilitatorAnswerOffline(
                        facilitatorAnswer
                    )

                    SurveyIntent.GetFacilitatorForm -> getFacilitatorForm()

                    SurveyIntent.SetFacilitatorForm -> setFacilitatorForm(facilitatorForm)
                }
            }
        }
    }

    private fun getUserId() {
        runBlocking {
            viewModelScope.launch {
                surveyUseCase.userId().collect { id ->
                    if (id != null) {
                        facilitatorAnswer.userId = id
                    }
                }
            }
        }
    }

    private fun getAllFarmersById(id: Int) {
        viewModelScope.launch {
            _state.value = SurveyState.Loading
            _state.value = when (val resource = surveyUseCase.getAllFarmersById(id)) {
                is Resource.Success -> {
                    farmersList = resource.data?.map { it.toFarmerDataItem() }
                    SurveyState.Success(farmersList)
                }

                is Resource.Error -> SurveyState.Error(resource.error)
            }
        }
    }

    private fun getFacilitatorForm(id: Int) {
        viewModelScope.launch {
            val resource = surveyUseCase.getFacilitatorForm(id)
            if (resource is Resource.Success) {
                val data = resource.data
                if (data != null) {
                    facilitatorForm.ID = data.ID
                    facilitatorForm.description = data.description
                    facilitatorForm.fields = data.fields
                    facilitatorForm.name = data.name
                    setFacilitatorForm(facilitatorForm)
                }
            }
        }
    }

    private fun submitFacilitatorAnswer(facilitatorAnswer: FacilitatorAnswer) {
        viewModelScope.launch {
            _state.value = SurveyState.Loading
            _state.value =
                when (val resource = surveyUseCase.submitFacilitatorAnswer(facilitatorAnswer)) {
                    is Resource.Success -> {
                        SurveyState.Success(resource.data)
                    }

                    is Resource.Error -> SurveyState.Error(resource.error)
                }
        }
    }

    private fun insertFacilitatorAnswerOffline(facilitatorAnswer: FacilitatorAnswer) {
        val facilitatorAnswerLocal = com.cultivaet.hassad.domain.model.local.FacilitatorAnswer(
            facilitatorAnswer.formId,
            facilitatorAnswer.farmerId,
            facilitatorAnswer.geolocation,
            Utils.toJson(facilitatorAnswer.answers),
            facilitatorAnswer.userId,
        )

        viewModelScope.launch { surveyUseCase.insertFacilitatorAnswer(facilitatorAnswerLocal) }
    }

    private fun setFacilitatorForm(form: Form) {
        viewModelScope.launch {
            surveyUseCase.setFacilitatorForm(Gson().toJson(form))
        }
    }

    private fun getFacilitatorForm() {
        viewModelScope.launch {
            surveyUseCase.getFacilitatorForm().collect { facilitatorFormJson ->
                if (facilitatorFormJson.isNullOrEmpty()) {
                    getFacilitatorForm(facilitatorAnswer.userId)
                } else {
                    val form = Utils.fromJson<Form>(facilitatorFormJson)
                    facilitatorForm.ID = form.ID
                    facilitatorForm.description = form.description
                    facilitatorForm.fields = form.fields
                    facilitatorForm.name = form.name
                }
            }
        }
    }

    fun uploadImage(bitmap: Bitmap, size: Int, currentIndex: Int) {
        val imageFile = application.applicationContext.createTempImageFile()
        imageFile
            .outputStream()
            .use { outputStream -> bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) }
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        viewModelScope.launch {
            val resource = surveyUseCase.uploadImage(imagePart)
            if (resource is Resource.Success) {
                val data = resource.data

                if (currentIndex == size - 2) {
                    uuidImages += data?.uuid
                    facilitatorAnswer.answers[indexOfImages].apply {
                        this.body = uuidImages
                    }
                    _state.value = SurveyState.Success(data)
                    Log.d("TAG", "uploadImage: $uuidImages")
                } else {
                    uuidImages += "${data?.uuid},"
                }
            }
        }
    }
}