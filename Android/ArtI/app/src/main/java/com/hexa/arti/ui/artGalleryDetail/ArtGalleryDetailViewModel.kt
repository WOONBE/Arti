package com.hexa.arti.ui.artGalleryDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.artmuseum.ArtGalleryResponse
import com.hexa.arti.data.model.artmuseum.MyGalleryThemeItem
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.repository.ArtGalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtGalleryDetailViewModel @Inject constructor(
    private val galleryRepository: ArtGalleryRepository
): ViewModel() {

    private var _currentPage = 0

    private val _galleryDetail = MutableLiveData<List<MyGalleryThemeItem>>()
    val galleryDetail : LiveData<List<MyGalleryThemeItem>> = _galleryDetail

    fun getGallery(galleryId : Int){
        viewModelScope.launch {
            galleryRepository.getArtGalleryThemes(galleryId).onSuccess {
                _galleryDetail.value = it
            }.onFailure { error ->
                if (error is ApiException) {
                    Log.d("확인", "실패 ${error.code} ${error.message}")
                }

            }
        }
    }

    fun getPageNum() : Int{
        return _currentPage
    }
    fun updatePageNum(page : Int){
        _currentPage = page
    }
}