package com.hexa.arti.ui.search.artist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.search.Artist
import com.hexa.arti.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistBannerViewModel @Inject constructor(
    private val artistRepository: ArtistRepository
) : ViewModel() {
    private val _resultArtists = MutableLiveData<List<Artist>>()
    val resultArtists = _resultArtists

    fun getRandomArtists(){
        viewModelScope.launch {
            artistRepository.getRandomArtists().onSuccess { response ->
                _resultArtists.value = response
                Log.d("확인","성공 ${response}")
            }.onFailure { error ->
                Log.d("확인","실패 ${error}")
                if (error is ApiException) {
                    _resultArtists.value = emptyList()
                }
            }
        }
    }
}