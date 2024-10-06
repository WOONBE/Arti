package com.hexa.arti.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.repository.MemberRepository
import com.hexa.arti.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun changeNickname(nickname: String){
        viewModelScope.launch {
            userRepository.postChangeNickname(nickname).onSuccess {
                Log.d("확인", "changeNickname: $it")
            }.onFailure {

            }
        }
    }


    fun changePass(currentPassword : String,newPassword : String, confirmationPassword : String){
        viewModelScope.launch {
            userRepository.postChangePass(currentPassword, newPassword = newPassword, confirmationPassword = confirmationPassword).onSuccess {
                Log.d("확인", "changePass: $it")
            }.onFailure {

            }
        }
    }
}