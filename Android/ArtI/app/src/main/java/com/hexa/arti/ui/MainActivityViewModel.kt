package com.hexa.arti.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")

    private val _fragmentState = MutableLiveData<Int>()
    val fragmentState: MutableLiveData<Int> = _fragmentState

    // JWT 토큰 읽기
    fun getJwtToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[JWT_TOKEN_KEY] ?: ""
        }
    }

    fun setFragmentState(state: Int){
        _fragmentState.value = state
    }

    companion object {
        const val PORTFOLIO_FRAGMENT = 4
    }
}