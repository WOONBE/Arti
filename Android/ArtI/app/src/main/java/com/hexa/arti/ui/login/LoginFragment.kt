package com.hexa.arti.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentHomeBinding
import com.hexa.arti.databinding.FragmentLoginBinding


class LoginFragment :
    BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun init() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            Toast.makeText(requireContext(),"로그인",Toast.LENGTH_SHORT).show()
        }
        binding.signBtn.setOnClickListener {
            Toast.makeText(requireContext(),"회원가입",Toast.LENGTH_SHORT).show()
        }
    }
}

