package com.hexa.arti.ui.search

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.search.Art
import com.hexa.arti.databinding.FragmentGenreDetailBinding
import com.hexa.arti.ui.search.adapter.ArtAdapter

class GenreDetailFragment :
    BaseFragment<FragmentGenreDetailBinding>(R.layout.fragment_genre_detail) {

    val artAdapter = ArtAdapter {
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
            Art(id = 0, title = "0번"),
            Art(id = 1, title = "1번"),
            Art(id = 2, title = "2번"),
            Art(id = 3, title = "3번"),
            Art(id = 4, title = "4번"),
            Art(id = 5, title = "5번"),
            Art(id = 6, title = "6번"),
            Art(id = 7, title = "7번"),
            Art(id = 8, title = "8번"),
        )

        artAdapter.submitList(mockData)
    }
}