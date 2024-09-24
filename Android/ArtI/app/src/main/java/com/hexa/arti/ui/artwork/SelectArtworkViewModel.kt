package com.hexa.arti.ui.artwork

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.repository.ArtWorkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SelectArtworkViewModel"
@HiltViewModel
class SelectArtworkViewModel @Inject constructor(
    private val artWorkRepository: ArtWorkRepository
) : ViewModel() {

    private val _artworkResult = MutableLiveData<List<Artwork>>()
    val artworkResult: LiveData<List<Artwork>> = _artworkResult

    fun getSearchArtWork(search: String){
        Log.d(TAG, "getSearchArtWork: $search")
        viewModelScope.launch {
            artWorkRepository.getArtWorkByString(search).onSuccess { response ->
                _artworkResult.value = response
                Log.d(TAG, "response: $response")
            }.onFailure { error ->
                if (error is ApiException) {
                    Log.d(TAG, "response: ${error.code}")
                    _artworkResult.value = emptyList()
                }
            }
        }
    }

}