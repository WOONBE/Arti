package com.hexa.arti.ui.home

import android.util.Log
import androidx.fragment.app.viewModels
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentHomeBinding
import com.hexa.arti.ui.home.adapter.ViewpageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {


    private val viewModel: HomeViewModel by viewModels()

    override fun init() {
        binding.viewpager2.adapter = ViewpageAdapter(
            onPlayClick = { itemNumber ->
                Log.d("확인", "클릭 확인요 ${itemNumber}")
            },
            onSliding = {
                binding.viewpager2.isUserInputEnabled = false
            },
            onNormal = {
                binding.viewpager2.isUserInputEnabled = true
            }
        )
    }


}