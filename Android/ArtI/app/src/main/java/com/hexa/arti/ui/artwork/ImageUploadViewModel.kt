package com.hexa.arti.ui.artwork

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hexa.arti.network.ArtWorkUpload
import com.hexa.arti.repository.ArtWorkUploadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

private const val TAG = "ImageUploadViewModel"

@HiltViewModel
class ImageUploadViewModel @Inject constructor(
    private val artWorkUploadRepository: ArtWorkUploadRepository
) : ViewModel() {


    private val _imageResponse = MutableLiveData<String>("")
    val imageResponse: LiveData<String> = _imageResponse

    fun makeImage(contentImage : MultipartBody.Part, styleImage : Int)
    {
        Log.d(TAG, "makeImage: ${styleImage}")
        Log.d(TAG, "makeImage: ${makeStyleImageIdPart(styleImage)}")


        viewModelScope.launch {
            artWorkUploadRepository.postMakeAIImage(contentImage = contentImage, styleImage = makeStyleImageIdPart(styleImage)).onSuccess {
                    response ->
                Log.d(TAG, "ImageUploadViewModel: ${response}")
                _imageResponse.value = response.image_url

            }.onFailure {
                    error ->
                Log.d(TAG, "ImageUploadViewModel: ${error}")
            }
        }
    }

    private fun makeStyleImageIdPart(styleImageId: Int): RequestBody {
        return styleImageId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    }

}