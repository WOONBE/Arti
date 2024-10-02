package com.hexa.arti.ui.search.artwork

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtBannerBinding
import com.hexa.arti.ui.search.adapter.ArtworkAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtBannerFragment : BaseFragment<FragmentArtBannerBinding>(R.layout.fragment_art_banner) {

    private val viewModel: ArtBannerViewModel by viewModels()

    val artAdapter = ArtworkAdapter {
        Log.d("확인", "작품 클릭 확인")
    }


    override fun init() {
        binding.rvArt.adapter = artAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        initObserve()
        initViews()
    }

    private fun initObserve() {
        viewModel.resultArtworks.observe(viewLifecycleOwner) {
            artAdapter.submitList(it)
        }
    }

    private fun initViews() {
        viewModel.getRecommendArtworks(1)
    }

}