package com.hexa.arti.ui

import com.hexa.arti.ui.login.LoginFragment
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.navigation.fragment.NavHostFragment
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

    }

    override fun setupBinding(binding: ActivityLoginBinding) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.login_ft) as NavHostFragment
        val navController = navHostFragment.navController
    }
}