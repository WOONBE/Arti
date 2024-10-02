package com.hexa.arti.ui.search.artwork

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArtBannerViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _resultArtworks = MutableLiveData<List<Artwork>>()
    val resultArtworks = _resultArtworks

    fun getRecommendArtworks(userId: Int) {
        viewModelScope.launch {
            homeRepository.getRecommendArtworks(userId).onSuccess {
                _resultArtworks.value = it
            }.onFailure { error ->
                if (error is ApiException) {
                    _resultArtworks.value = emptyList()
                }
            }
        }
    }

}