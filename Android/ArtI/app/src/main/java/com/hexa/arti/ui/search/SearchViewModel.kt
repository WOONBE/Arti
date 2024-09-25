package com.hexa.arti.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.search.Artist
import com.hexa.arti.repository.ArtWorkRepository
import com.hexa.arti.repository.ArtistRepository
import com.hexa.arti.ui.search.paging.ArtworkPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val artWorkRepository: ArtWorkRepository,
    private val artistRepository: ArtistRepository,
) : ViewModel() {

    private val _artistResult = MutableLiveData<List<Artist>>()
    val artistResult: LiveData<List<Artist>> = _artistResult

    private val _artworkResult = MutableLiveData<List<Artwork>>()
    val artworkResult: LiveData<List<Artwork>> = _artworkResult

    var state = SearchFragment.BASE_STATE

    val artworkPagingData = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ArtworkPagingSource(artWorkRepository, "query") }
    ).liveData.cachedIn(viewModelScope)

    fun getArtWorkById(id: Int) {
        viewModelScope.launch {
            artWorkRepository.getArtWorkById(id).onSuccess { response ->
                Log.d("확인", "성공 ${response}")
            }.onFailure { error ->
                if (error is ApiException) {
                    Log.d("확인", "실패 ${error.code} ${error.message}")
                }
            }
        }
    }

    fun getArtworkByString(keyword: String) {
        viewModelScope.launch {
            artWorkRepository.getArtWorksByString(keyword).onSuccess { response ->
                _artworkResult.value = response
            }.onFailure { error ->
                if (error is ApiException) {
                    _artworkResult.value = emptyList()
                }
            }
        }
    }

    fun getArtistByString(keyword: String) {
        viewModelScope.launch {
            artistRepository.getArtist(keyword).onSuccess { response ->
                _artistResult.value = response
            }.onFailure { error ->
                if (error is ApiException) {
                    _artistResult.value = emptyList()
                }
            }
        }
    }

}