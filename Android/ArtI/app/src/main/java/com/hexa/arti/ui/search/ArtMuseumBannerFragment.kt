package com.hexa.arti.ui.search

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.search.ArtMuseum
import com.hexa.arti.databinding.FragmentArtMuseumBannerBinding
import com.hexa.arti.ui.search.adapter.ArtMuseumAdapter

class ArtMuseumBannerFragment :
    BaseFragment<FragmentArtMuseumBannerBinding>(R.layout.fragment_art_museum_banner) {

    private val artMuseumAdapter = ArtMuseumAdapter {
        Log.d("확인", "아이템 클릭 확인")
    }

    override fun init() {
        initVies()
    }

    private fun initVies() {
        binding.rvArtMuseum.adapter = artMuseumAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val mockData = listOf(
            ArtMuseum(id = 1, title = "하하", artist = "히히"),
            ArtMuseum(id = 2, title = "하하", artist = "히히"),
            ArtMuseum(id = 3, title = "하하", artist = "히히"),
            ArtMuseum(id = 4, title = "하하", artist = "히히"),
            ArtMuseum(id = 5, title = "하하", artist = "히히"),
            ArtMuseum(id = 6, title = "하하", artist = "히히"),
            ArtMuseum(id = 7, title = "하하", artist = "히히"),
            ArtMuseum(id = 8, title = "하하", artist = "히히"),
        )

        artMuseumAdapter.submitList(mockData)
    }

}