package com.hexa.arti.ui.search

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.search.Artist
import com.hexa.arti.databinding.FragmentArtistBannerBinding
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
            Artist(id = 0, name = "잠만보1"),
            Artist(id = 1, name = "잠만보2"),
            Artist(id = 2, name = "잠만보3"),
            Artist(id = 3, name = "잠만보4"),
            Artist(id = 4, name = "잠만보5"),
            Artist(id = 5, name = "잠만보6"),
            Artist(id = 6, name = "잠만보7"),
            Artist(id = 7, name = "잠만보8"),
            Artist(id = 8, name = "잠만보9"),
        )

        artistAdapter.submitList(mockData)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}