package com.hexa.arti.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentHomeBinding
import com.hexa.arti.databinding.FragmentLoginBinding
import com.hexa.arti.ui.LoginActivity
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.ui.signup.SignUpFragment
import com.hexa.arti.util.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class LoginFragment :
    BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun init() {
        initObserve()
        with(binding) {
            loginBtn.setOnClickListener {
                startActivity(Intent(requireContext(), MainActivity::class.java))
//                if(!loginIdEt.text.isNullOrBlank() && !loginPwEt.text.isNullOrBlank() ){
//                    loginActivity.showLoadingDialog()
//                    loginViewModel.updateEmail(loginIdEt.text.toString())
//                    loginViewModel.updatePass(loginPwEt.text.toString())
//                    loginViewModel.login()
//                }
//                else{
//                    makeToast("ID와 PW를 입력해주세요")
//                }

            }
            signBtn.setOnClickListener {
                loginActivity.moveSignUp()
            }
        }
    }

    private fun initObserve() {
        loginViewModel.loginStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                1 -> {
                    loginActivity.hideLoadingDialog()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    loginActivity.finish()
                }

                2 -> {
                    loginActivity.hideLoadingDialog()
                    makeToast("로그인 실패")
                }
                else -> {

                }
            }
        }
    }
}

