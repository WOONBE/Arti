package com.hexa.arti.ui.search

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.ApplicationClass.Companion.REPRESENT_ARTWORKS
import com.hexa.arti.config.ApplicationClass.Companion.REPRESENT_ARTWORKS_KOR
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.search.Genre
import com.hexa.arti.databinding.FragmentGenreBannerBinding
import com.hexa.arti.ui.search.adapter.GenreAdapter


class GenreBannerFragment :
    BaseFragment<FragmentGenreBannerBinding>(R.layout.fragment_genre_banner) {

    private val genreAdapter = GenreAdapter {
        goToGenreDetailFragment()
    }


    override fun init() {
        initViews()
    }

    private fun initViews() {

        binding.rvGenre.adapter = genreAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val representGenres = mutableListOf<Genre>()

        Log.d("확인", "장르 사이즈 확인 ${REPRESENT_ARTWORKS.size} ${REPRESENT_ARTWORKS_KOR.size}")

        for (index in 0..<REPRESENT_ARTWORKS.size) {
            representGenres.add(
                Genre(
                    id = index,
                    title = REPRESENT_ARTWORKS[index],
                    titleKor = REPRESENT_ARTWORKS_KOR[index]
                )
            )
        }

        genreAdapter.submitList(representGenres)
    }

    private fun goToGenreDetailFragment() {
        findNavController().navigate(R.id.action_genreBannerFragment_to_genreDetailFragment)
    }
}