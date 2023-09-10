package com.cultivaet.hassad.ui.main

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cultivaet.hassad.core.extension.createTempImageFile
import com.cultivaet.hassad.core.source.remote.Resource
import com.cultivaet.hassad.core.util.Utils
import com.cultivaet.hassad.domain.model.remote.requests.Answer
import com.cultivaet.hassad.domain.model.remote.requests.FacilitatorAnswer
import com.cultivaet.hassad.domain.model.remote.requests.Farmer
import com.cultivaet.hassad.domain.usecase.MainUseCase
import com.cultivaet.hassad.ui.main.intent.MainIntent
import com.cultivaet.hassad.ui.main.viewstate.MainState
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
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
class MainViewModel(
    private val application: Application,
    private val mainUseCase: MainUseCase
) : ViewModel() {
    val mainIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState> = _state
    internal var userId: Int = -1

    init {
        getUserId()
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            mainIntent.consumeAsFlow().collect {
                when (it) {
                    MainIntent.SubmitOfflineFacilitatorAnswersList -> submitOfflineFacilitatorAnswersList()

                    MainIntent.SubmitOfflineFarmersList -> submitOfflineFarmersList()
                }
            }
        }
    }

    private fun getUserId() {
        runBlocking {
            viewModelScope.launch {
                mainUseCase.userId().collect { id ->
                    if (id != null) {
                        userId = id
                    }
                }
            }
        }
    }

    private fun submitFacilitatorAnswer(
        facilitatorAnswer: FacilitatorAnswer,
        facilitatorAnswerDb: com.cultivaet.hassad.domain.model.local.FacilitatorAnswer,
        isLastElement: Boolean
    ) {
        viewModelScope.launch {
            val resource = mainUseCase.submitFacilitatorAnswer(facilitatorAnswer)
            if (resource is Resource.Success) {
                deleteFacilitatorAnswerOffline(facilitatorAnswerDb)
                if (isLastElement) _state.value = MainState.SuccessSurvey
            }
        }
    }

    private fun deleteFacilitatorAnswerOffline(facilitatorAnswer: com.cultivaet.hassad.domain.model.local.FacilitatorAnswer) {
        viewModelScope.launch { mainUseCase.deleteFacilitatorAnswer(facilitatorAnswer) }
    }

    private fun submitOfflineFacilitatorAnswersList() {
        viewModelScope.launch {
            val facilitatorAnswersList = mainUseCase.getFacilitatorAnswers()
            Log.d("TAG", "getFacilitatorAnswers: $facilitatorAnswersList")
            for ((index, value) in facilitatorAnswersList.withIndex()) {
                val answers = Utils.fromJson<Array<Answer>>(value.answers).asList().toMutableList()
                val answersWithImages = answers.find { it.type == "images" }
                if (answersWithImages != null) {
                    val indexOfAnswerImage = answers.indexOf(answersWithImages)
                    val list = getUUIDsFromBitmaps(
                        Utils.fromJson<Array<Bitmap>>(answersWithImages.body.toString()).asList()
                    ).await()
                    answers[indexOfAnswerImage].body = list.map { it.await() }.joinToString(", ")
                }
                Log.d("TAG", "submitOfflineFacilitatorAnswersList: $answers")
                submitFacilitatorAnswer(
                    FacilitatorAnswer(
                        userId = value.userId,
                        formId = value.formId,
                        farmerId = value.farmerId,
                        geolocation = value.geolocation,
                        answers = answers,
                        type = value.type
                    ),
                    value,
                    index == facilitatorAnswersList.size - 1
                )
            }
        }
    }

    fun getUUIDsFromBitmaps(bitmaps: List<Bitmap>): Deferred<List<Deferred<String>>> {
        return viewModelScope.async(Dispatchers.IO) {
            val listOfDeferred = mutableListOf<Deferred<String>>()
            bitmaps.forEachIndexed { index, bitmap ->
                if (index < bitmaps.size - 1) {
                    listOfDeferred.add(uploadImage(bitmap))
                }
            }
            listOfDeferred
        }
    }

    fun uploadImage(bitmap: Bitmap): Deferred<String> {
        return viewModelScope.async(Dispatchers.IO) {
            val imageFile = application.applicationContext.createTempImageFile()
            imageFile
                .outputStream()
                .use { outputStream ->
                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        80,
                        outputStream
                    )
                }
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val resource = mainUseCase.uploadImage(imagePart)
            (if (resource is Resource.Success) resource.data?.uuid else "").toString()
        }
    }

    private fun submitFarmer(
        farmer: Farmer,
        farmerDb: com.cultivaet.hassad.domain.model.local.Farmer,
        isLastElement: Boolean
    ) {
        viewModelScope.launch {
            val resource = mainUseCase.submitAddFarmer(farmer)
            if (resource is Resource.Success) {
                deleteFarmerOffline(farmerDb)
                if (isLastElement) _state.value = MainState.SuccessFarmers
            }
        }
    }

    private fun deleteFarmerOffline(farmer: com.cultivaet.hassad.domain.model.local.Farmer) {
        viewModelScope.launch { mainUseCase.deleteFarmer(farmer) }
    }

    private fun submitOfflineFarmersList() {
        viewModelScope.launch {
            val farmersList = mainUseCase.getFarmers()
            Log.d("TAG", "getFarmers: $farmersList")
            for ((index, value) in farmersList.withIndex()) {
                submitFarmer(
                    Farmer(
                        firstName = value.firstName,
                        lastName = value.lastName,
                        phoneNumber = value.phoneNumber,
                        gender = value.gender,
                        age = value.age,
                        address = value.address,
                        landArea = value.landArea,
                        ownership = value.ownership,
                        geolocation = value.geolocation,
                        ZeroDay = value.ZeroDay,
                        cropType = value.cropType,
                        cropsHistory = value.cropsHistory,
                        facilitatorId = value.facilitatorId
                    ),
                    value,
                    index == farmersList.size - 1
                )
            }
        }
    }

    fun loggedInState(goToLogin: () -> Unit) {
        runBlocking {
            viewModelScope.launch {
                mainUseCase.userLoggedOut()
                goToLogin()
            }
        }
    }
}