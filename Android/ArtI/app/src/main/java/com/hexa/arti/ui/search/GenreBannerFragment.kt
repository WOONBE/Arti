package com.hexa.arti.ui.search

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.search.Genre
import com.hexa.arti.databinding.FragmentGenreBannerBinding
import com.hexa.arti.ui.search.adapter.GenreAdapter


class GenreBannerFragment :
    BaseFragment<FragmentGenreBannerBinding>(R.layout.fragment_genre_banner) {

    private val genreAdapter = GenreAdapter {
        Log.d("확인", " 이게실행되는거임?")
        goToGenreDetailFragment()
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun init() {
        initViews()
    }

    private fun initViews() {

        binding.rvGenre.adapter = genreAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val mockData = listOf(
            Genre(id = 0, title = "제목1"),
            Genre(id = 1, title = "제목1"),
            Genre(id = 2, title = "제목1"),
            Genre(id = 3, title = "제목1"),
            Genre(id = 4, title = "제목1"),
            Genre(id = 5, title = "제목1"),
            Genre(id = 6, title = "제목1"),
            Genre(id = 7, title = "제목1"),
        )

        genreAdapter.submitList(mockData)
    }

    private fun goToGenreDetailFragment() {
        findNavController().navigate(R.id.action_genreBannerFragment_to_genreDetailFragment)
    }
}