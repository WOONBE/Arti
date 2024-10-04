package com.hexa.arti.ui.search.museum

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.artmuseum.GetTotalThemeResponse
import com.hexa.arti.repository.ArtGalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtMuseumViewModel @Inject constructor(
    private val galleryRepository: ArtGalleryRepository
) : ViewModel() {

    private val _resultTotalTheme = MutableLiveData<List<GetTotalThemeResponse>>()
    val resultTotalTheme = _resultTotalTheme

    fun getTotalThemes(galleryId: Int) {
        viewModelScope.launch {
            galleryRepository.getThemeWithArtworks(galleryId).onSuccess {
                _resultTotalTheme.value = it
            }.onFailure {
                _resultTotalTheme.value = emptyList()
            }
        }
    }

}