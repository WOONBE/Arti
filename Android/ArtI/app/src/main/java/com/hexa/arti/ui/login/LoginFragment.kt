package com.hexa.arti.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentHomeBinding
import com.hexa.arti.databinding.FragmentLoginBinding
import com.hexa.arti.ui.LoginActivity
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.util.navigate


class LoginFragment :
    BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {


    private lateinit var loginActivity: LoginActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }
    override fun init() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            Toast.makeText(requireContext(),"로그인",Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(),MainActivity::class.java))
            loginActivity.finish()
        }
        binding.signBtn.setOnClickListener {
            Toast.makeText(requireContext(),"회원가입",Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }
}

