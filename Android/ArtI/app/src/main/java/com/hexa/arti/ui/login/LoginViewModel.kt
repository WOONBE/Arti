package com.hexa.arti.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    private val _email = MutableLiveData<String>("")
    val email: LiveData<String> = _email

    private val _pass = MutableLiveData<String>("")
    val pass: LiveData<String> = _pass

    private val _loginStatus = MutableLiveData(0)
    val loginStatus: LiveData<Int> = _loginStatus

    fun login() {
        viewModelScope.launch {
            _loginStatus.value = 1
        }
    }

    fun updateEmail(em: String) {
        _email.value = em
    }

    fun updatePass(pw: String) {
        _pass.value = pw
    }

}