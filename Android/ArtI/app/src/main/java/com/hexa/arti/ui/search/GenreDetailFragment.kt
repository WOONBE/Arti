package com.hexa.arti.ui.search

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.databinding.FragmentGenreDetailBinding
import com.hexa.arti.ui.search.adapter.ArtworkAdapter

class GenreDetailFragment :
    BaseFragment<FragmentGenreDetailBinding>(R.layout.fragment_genre_detail) {

    val artAdapter = ArtworkAdapter {
        Log.d("확인", "클릭 확인")
    }


    override fun init() {
        initViews()
    }

    private fun initViews(){
        binding.rvArt.adapter = artAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val mockData = listOf(
            Artwork(artworkId = 0, title = "0번","","",""),
            Artwork(artworkId = 1, title = "1번","","",""),
            Artwork(artworkId = 2, title = "2번","","",""),
            Artwork(artworkId = 3, title = "3번","","",""),
            Artwork(artworkId = 4, title = "4번","","",""),
            Artwork(artworkId = 5, title = "5번","","",""),
            Artwork(artworkId = 6, title = "6번","","",""),
            Artwork(artworkId = 7, title = "7번","","",""),
            Artwork(artworkId = 8, title = "8번","","",""),

        )

        artAdapter.submitList(mockData)
    }
}