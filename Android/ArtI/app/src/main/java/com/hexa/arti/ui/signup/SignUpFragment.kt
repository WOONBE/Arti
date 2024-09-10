package com.hexa.arti.ui.signup

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentSignUpBinding
import com.hexa.arti.ui.LoginActivity
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {
    override fun init() {
    }
    private lateinit var loginActivity: LoginActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            signCancelBtn.setOnClickListener {
                loginActivity.moveLogin()
            }
            signBtn.setOnClickListener {
                loginActivity.moveLogin()
            }
            signEmailBtn.setOnClickListener {
                signCertificationTv.visibility = View.VISIBLE
                signCertificationEt.visibility = View.VISIBLE
                signCertificationBtn.visibility = View.VISIBLE
            }
            signBackBtn.setOnClickListener {
                loginActivity.moveLogin()
            }
        }
    }
}