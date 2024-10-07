package com.hexa.arti.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.home.GetRecommendGalleriesResponse
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _resultGalleries = MutableLiveData<List<GetRecommendGalleriesResponse>>()
    val resultGalleries = _resultGalleries

    fun getRecommendGalleries(userId: Int) {
        viewModelScope.launch {
            homeRepository.getRecommendGalleries(userId).onSuccess {
                Log.d("확인", "홈 성공 ${userId}")
                _resultGalleries.value = it
            }.onFailure { error ->
                Log.d("확인", "홈 실패 ${userId}")
                if (error is ApiException) {
                    _resultGalleries.value = emptyList()
                }
            }
        }

    }
}