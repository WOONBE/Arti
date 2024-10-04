package com.hexa.arti.ui.survey

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.artmuseum.CreateThemeDto
import com.hexa.arti.data.model.artmuseum.GalleryRequest
import com.hexa.arti.data.model.artmuseum.GalleryResponse
import com.hexa.arti.data.model.artmuseum.ThemeResponseItem
import com.hexa.arti.repository.ArtGalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class CreateGalleryViewModel @Inject constructor(
    private val galleryRepository: ArtGalleryRepository
) : ViewModel() {

    private val _thumbnail = MutableLiveData<MultipartBody.Part>()
    val thumbnail : LiveData<MultipartBody.Part> = _thumbnail

    private val _galleryResponse = MutableLiveData<GalleryResponse>()
    val galleryResponse : LiveData<GalleryResponse> = _galleryResponse

    private val _themeDto = MutableLiveData<ThemeResponseItem>()
    val themeDto : LiveData<ThemeResponseItem> = _themeDto

    fun createGallery(galleryDto : GalleryRequest){
        viewModelScope.launch {
            galleryRepository.postGallery(_thumbnail.value!!,galleryDto).onSuccess {
                _galleryResponse.value = it
                Log.d("확인", "galleryRepository: $it")
            }.onFailure {
                Log.d("확인", "galleryRepository: $it")
            }
        }

    }

    fun createTheme(id : Int, theme : String){
        Log.d("확인", "createTheme: $id $theme")
        viewModelScope.launch {
            galleryRepository.postTheme(CreateThemeDto(id,theme)).onSuccess {
                _themeDto.value = it
                Log.d("확인", "galleryRepository: $it")
            }.onFailure {
                Log.d("확인", "errrr galleryRepository: $it")
            }
        }
    }

    fun updateThumbnail(image :MultipartBody.Part){
        _thumbnail.value = image
    }
}