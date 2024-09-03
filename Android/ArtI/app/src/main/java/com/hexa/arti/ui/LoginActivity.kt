package com.hexa.arti.ui

import com.hexa.arti.ui.login.LoginFragment
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.hexa.arti.R
import com.hexa.arti.config.BaseActivity
import com.hexa.arti.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        initView()
    }

    private fun initView(){
            supportFragmentManager.beginTransaction()
                .replace(R.id.login_ft, LoginFragment())
                .commit()
    }

    override fun setupBinding(binding: ActivityLoginBinding) {

    }
}