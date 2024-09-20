package com.hexa.arti.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private val _pass = MutableLiveData<String>("")
    val pass: LiveData<String> = _pass

    private val _loginStatus = MutableLiveData(0)
    val loginStatus: LiveData<Int> = _loginStatus

    fun login() {
        viewModelScope.launch {
            loginRepository.postLogin(_email.value.toString(), _pass.value.toString()).onSuccess {
                response ->
                Log.d(TAG, "login: ${response}")
                _loginStatus.value = 1
            }.onFailure {
                error ->
                Log.d(TAG, "login: ${error}")
                _loginStatus.value = 2
            }

        }
    }

    fun updateEmail(em: String) {
        _email.value = em
    }

    fun updatePass(pw: String) {
        _pass.value = pw
    }

}