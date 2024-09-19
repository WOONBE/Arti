package com.hexa.arti.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.model.signup.SignUpModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _signUpModel = MutableLiveData<SignUpModel>(SignUpModel("", "", ""))
    val signUpModel: LiveData<SignUpModel> = _signUpModel

    private val _signStatus = MutableLiveData<Int>(0)
    val signStatus: LiveData<Int> = _signStatus
    fun signUp() {
        // retrofit
        viewModelScope.launch {
            _signStatus.value = 1
        }
    }

    fun updateNickName(nickName: String) {
        _signUpModel.value = _signUpModel.value?.copy(nickName = nickName)
    }

    fun updateEmail(email: String) {
        _signUpModel.value = _signUpModel.value?.copy(email = email)
    }

    fun updatePassword(pass: String) {
        _signUpModel.value = _signUpModel.value?.copy(password = pass)
    }
}