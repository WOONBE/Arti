package com.hexa.arti.ui.search

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.databinding.FragmentArtBannerBinding
import com.hexa.arti.ui.search.adapter.ArtworkAdapter


class ArtBannerFragment : BaseFragment<FragmentArtBannerBinding>(R.layout.fragment_art_banner) {

    val artAdapter = ArtworkAdapter {
        Log.d("확인", "작품 클릭 확인")
    }


    override fun init() {
        binding.rvArt.adapter = artAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val mockData = listOf(
            Artwork(artworkId = 0, title = "0번", imageUrl = "","",""),
            Artwork(artworkId = 0, title = "1번", imageUrl = "","",""),
            Artwork(artworkId = 0, title = "2번", imageUrl = "","",""),
            Artwork(artworkId = 0, title = "3번", imageUrl = "","",""),
            Artwork(artworkId = 0, title = "4번", imageUrl = "","",""),
            Artwork(artworkId = 0, title = "5번", imageUrl = "","",""),
            Artwork(artworkId = 0, title = "6번", imageUrl = "","",""),
            Artwork(artworkId = 0, title = "7번", imageUrl = "","",""),
            Artwork(artworkId = 0, title = "8번", imageUrl = "","",""),
        )

        artAdapter.submitList(mockData)
    }
}