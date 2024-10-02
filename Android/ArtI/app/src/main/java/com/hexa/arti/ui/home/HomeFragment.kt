package com.hexa.arti.ui.home

import android.util.Log
import androidx.fragment.app.viewModels
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentHomeBinding
import com.hexa.arti.ui.home.adapter.ViewpageAdapter
import com.hexa.arti.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {


    private val viewModel: HomeViewModel by viewModels()
    private val viewpageAdapter = ViewpageAdapter(
        onPlayClick = { itemNumber ->
            Log.d("확인", "클릭 확인요 ${itemNumber}")
            val action = HomeFragmentDirections.actionHomeFragmentToArtGalleryDetailFragment(1)
            navigate(action)
        },
        onSliding = {
            binding.viewpager2.isUserInputEnabled = false
        },
        onNormal = {
            binding.viewpager2.isUserInputEnabled = true
        },
    )

    override fun init() {
        initObserve()
        initAdapter()
    }

    private fun initObserve() {
        viewModel.resultGalleries.observe(viewLifecycleOwner) {

        }
    }

    private fun initAdapter() {
        binding.viewpager2.adapter = viewpageAdapter

    }


}