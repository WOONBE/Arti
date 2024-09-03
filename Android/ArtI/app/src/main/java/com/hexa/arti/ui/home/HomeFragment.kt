package com.hexa.arti.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {


    private val viewModel: HomeViewModel by viewModels()

    override fun init() {

//        viewModel.testGet("2"){ modelList ->
//            println("확인 ${modelList.data}")
//        }
    }

}