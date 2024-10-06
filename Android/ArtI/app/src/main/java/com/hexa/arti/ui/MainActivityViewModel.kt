package com.hexa.arti.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hexa.arti.data.model.login.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
    private val MEMBER_ID_KEY = stringPreferencesKey("member_id")
    private val GALLERY_ID_KEY = stringPreferencesKey("gallery_id")
    private val REFRESH_TOEKN_KEY = stringPreferencesKey("refresh_token")

    private val _fragmentState = MutableLiveData<Int>()
    val fragmentState: MutableLiveData<Int> = _fragmentState

    // JWT 토큰 읽기
    fun getLoginData(): Flow<LoginResponse?> {
        return dataStore.data.map { preferences ->
            val token = preferences[JWT_TOKEN_KEY] ?: ""
            val memberId = preferences[MEMBER_ID_KEY]?.toIntOrNull() ?: -1
            val galleryId = preferences[GALLERY_ID_KEY]?.toIntOrNull() ?: -1
            val refreshToken = preferences[REFRESH_TOEKN_KEY] ?: ""
            if (token.isNotEmpty()) {
                LoginResponse(
                    token = token,
                    refreshToken = refreshToken,  // 저장하지 않은 값은 0 또는 기본값 설정
                    memberId = memberId,
                    galleryId = galleryId
                )
            } else {
                null
            }
        }
    }

    fun setFragmentState(state: Int){
        _fragmentState.value = state
    }

    companion object {
        const val PORTFOLIO_FRAGMENT = 4
    }
}