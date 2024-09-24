package com.hexa.arti.ui.login

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")

    private val _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private val _pass = MutableLiveData<String>("")
    val pass: LiveData<String> = _pass

    private val _loginStatus = MutableLiveData(0)
    val loginStatus: LiveData<Int> = _loginStatus

    fun login() {
//        viewModelScope.launch {
//            loginRepository.postLogin(_email.value.toString(), _pass.value.toString()).onSuccess {
//                response ->
//                Log.d(TAG, "login: ${response}")
//                Log.d(TAG, "login: ${response.token}")
//                saveJwtToken(response.token)
//                _loginStatus.value = 1
//            }.onFailure {
//                error ->
//                Log.d(TAG, "login: ${error}")
//                _loginStatus.value = 2
//            }
//        }
        _loginStatus.value = 1
    }

    // JWT 토큰 저장
    suspend fun saveJwtToken(token: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[JWT_TOKEN_KEY] = token
            }
        }
    }

    // JWT 토큰 읽기
    fun getJwtToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[JWT_TOKEN_KEY] ?: ""
        }
    }

    fun updateEmail(em: String) {
        _email.value = em
    }

    fun updatePass(pw: String) {
        _pass.value = pw
    }

}