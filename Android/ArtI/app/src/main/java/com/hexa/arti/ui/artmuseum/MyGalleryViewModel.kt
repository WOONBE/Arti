package com.hexa.arti.ui.artmuseum

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.artmuseum.CreateThemeDto
import com.hexa.arti.data.model.artmuseum.ThemeResponseItem
import com.hexa.arti.data.model.artmuseum.UpdateGalleryDto
import com.hexa.arti.repository.ArtGalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyGalleryViewModel"
@HiltViewModel
class MyGalleryViewModel @Inject constructor(
    private val galleryRepository: ArtGalleryRepository
): ViewModel() {

    private val _updateGalleryDto  = MutableLiveData<UpdateGalleryDto>()
    val updateGalleryDto : LiveData<UpdateGalleryDto> = _updateGalleryDto

    private val _updateThemeDto = MutableLiveData<ThemeResponseItem>()
    val updateThemeDto : LiveData<ThemeResponseItem> = _updateThemeDto

    fun createTheme(themeDto: CreateThemeDto){
        Log.d(TAG, "Init:  ${themeDto} ")
        viewModelScope.launch {
            galleryRepository.postTheme(themeDto).onSuccess {
                    response ->
                _updateThemeDto.value = response
                Log.d("Init", "$response deleteThemeDelete: success")
            }.onFailure {
                    error ->
                Log.d("Init", "$error deleteThemeDelete: fail")
            }
        }
    }

    fun updateGallery(galleryId : Int){
        viewModelScope.launch {
            galleryRepository.updateArtGallery(galleryId,_updateGalleryDto.value!!)
        }

    }
    fun deleteTheme(galleryId: Int, themeId: Int){
        viewModelScope.launch {
            galleryRepository.deleteTheme(galleryId,themeId).onSuccess {
                    response ->
                Log.d("Init", "$response deleteThemeDelete: success")
            }.onFailure {
                    error ->
                Log.d("Init", "$error deleteThemeDelete: fail")
            }
        }
    }
    fun deleteThemeDelete(themeId: Int, artworkId: Int){
        viewModelScope.launch {
            galleryRepository.deleteThemeArtWork(themeId,artworkId).onSuccess {
                response ->
                Log.d("Init", "$response deleteThemeDelete: success")
            }.onFailure {
                error ->
                Log.d("Init", "$error deleteThemeDelete: fail")
            }
        }
    }


    fun getGalleryDto(updateGalleryDto: UpdateGalleryDto){
        _updateGalleryDto.value = updateGalleryDto
    }


    fun updateGalleryName(name : String,galleryId : Int){
        _updateGalleryDto.value = _updateGalleryDto.value?.copy(name = name)
        updateGallery(galleryId)
    }
    fun updateGalleryThumbnail(){

    }
    fun updateGalleryDescription(description : String,galleryId : Int){
        _updateGalleryDto.value = _updateGalleryDto.value?.copy(description = description)
        updateGallery(galleryId)
    }

}