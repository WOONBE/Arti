package com.hexa.arti.ui.search.artist

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtistBannerBinding
import com.hexa.arti.ui.search.adapter.ArtistAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistBannerFragment :
    BaseFragment<FragmentArtistBannerBinding>(R.layout.fragment_artist_banner) {

    private val viewModel: ArtistBannerViewModel by viewModels()

    private val artistAdapter = ArtistAdapter {
        Log.d("확인", "아이템클릭 확인")
    }

    override fun init() {
        initObserve()
        initViews()

        viewModel.getRandomArtists()
    }

    private fun initObserve() {
        viewModel.resultArtists.observe(viewLifecycleOwner) {
            Log.d("확인", "결과는 ${it}")
            if (it.isEmpty()) {
                artistAdapter.submitList(emptyList())
            } else {
                artistAdapter.submitList(it)
            }
        }

    }

    private fun initViews() {
        binding.rvArtist.adapter = artistAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}