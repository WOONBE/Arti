package com.hexa.arti.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.portfolio.PortfolioGenre
import com.hexa.arti.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _resultGenres = MutableLiveData<List<PortfolioGenre>>()
    val resultGenres = _resultGenres

    fun getPortfolio(memberId: Int) {
        viewModelScope.launch {
            homeRepository.getPortfolio(memberId).onSuccess {
                _resultGenres.value = it
            }.onFailure {
                _resultGenres.value = emptyList()
            }
        }
    }

}