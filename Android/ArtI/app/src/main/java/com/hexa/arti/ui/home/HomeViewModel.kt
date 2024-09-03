package com.hexa.arti.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hexa.arti.data.UserListModel
import com.hexa.arti.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val testRepository: TestRepository
) : ViewModel() {





    fun testGet(
        page: String,
        onSuccess: (UserListModel) -> Unit
    ){

        viewModelScope.launch {
            testRepository.getUser(page).onSuccess { userListModel ->
                userListModel?.let {
                    onSuccess(userListModel)
                }
            }
        }
    }
}