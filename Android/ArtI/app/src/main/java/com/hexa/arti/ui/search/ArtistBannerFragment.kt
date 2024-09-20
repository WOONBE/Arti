package com.hexa.arti.ui.search

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.search.Artist
import com.hexa.arti.databinding.FragmentArtistBannerBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.ui.search.adapter.ArtistAdapter

class ArtistBannerFragment :
    BaseFragment<FragmentArtistBannerBinding>(R.layout.fragment_artist_banner) {


    private val artistAdapter = ArtistAdapter {
        Log.d("확인", "아이템클릭 확인")
    }

    override fun init() {
        initViews()
    }

    private fun initViews(){
        binding.rvArtist.adapter = artistAdapter

        val mockData = listOf(
            Artist(artistId = 0, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 1, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 2, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 3, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 4, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 5, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 6, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
        )

        artistAdapter.submitList(mockData)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}