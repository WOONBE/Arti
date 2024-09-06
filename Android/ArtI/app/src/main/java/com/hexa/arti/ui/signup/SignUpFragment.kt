package com.hexa.arti.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentSignUpBinding
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {
    override fun init() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signCancelBtn.setOnClickListener {
            popBackStack()
        }
        binding.signBtn.setOnClickListener {
            navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }
}