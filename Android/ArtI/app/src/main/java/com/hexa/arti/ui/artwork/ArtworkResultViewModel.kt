package com.hexa.arti.ui.artwork

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.repository.ArtWorkRepository
import com.hexa.arti.repository.ArtWorkUploadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.InputStream
import javax.inject.Inject

private const val TAG = "ArtworkResultViewModel"

@HiltViewModel
class ArtworkResultViewModel  @Inject constructor(
    private val artWorkUploadRepository: ArtWorkUploadRepository,
    private val artWorkRepository: ArtWorkRepository
) : ViewModel(){

    private val _imageUri = MutableLiveData<ByteArray>()
    val imageUri : LiveData<ByteArray> = _imageUri

    private val _artworkResult = MutableLiveData<Artwork>()
    val artworkResult: LiveData<Artwork> = _artworkResult


    fun getImageUri(imagePath : String) {
        viewModelScope.launch {
            artWorkUploadRepository.getImage(imagePath).onSuccess {
                response->
                val imageStream = response.byteStream()
                val byteArray = imageStream.readBytes() // InputStream을 ByteArray로 변환
                _imageUri.value = byteArray
            }.onFailure {
            }
        }
    }


    fun getArtWork(id : Int){
        viewModelScope.launch {
            artWorkRepository.getArtWorkById(id).onSuccess { response ->
                Log.d("확인", "성공 ${response}")
                _artworkResult.value = response
            }.onFailure { error ->
                if (error is ApiException) {
                    Log.d("확인", "실패 ${error.code} ${error.message}")
                    _artworkResult.value = Artwork(0,"","","","")
                }
            }
        }
    }
}