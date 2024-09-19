package com.hexa.arti.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.repository.ArtWorkRepository
import com.hexa.arti.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val artWorkRepository: ArtWorkRepository,
    private val artistRepository: ArtistRepository,
) : ViewModel() {

    fun getArtWorkById(id: Int){
        viewModelScope.launch {
            artWorkRepository.getArtWork(id).onSuccess { response ->
                Log.d("확인", "성공 ${response}")
            }.onFailure { error ->
                if(error is ApiException) {
                    Log.d("확인", "실패 ${error.code} ${error.message}")
                }
            }
        }
    }

    fun getArtistByString(keyword: String){
        Log.d("확인","요청 ${keyword}")
        viewModelScope.launch{
            artistRepository.getArtist(keyword).onSuccess { response ->
                Log.d("확인","성공 ${response}")
            }
                .onFailure { error ->
                    if(error is ApiException){
                        Log.d("확인", "실패 ${error}")
                    }
                }
        }
    }

}