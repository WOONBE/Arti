package com.hexa.arti.ui.search.genre

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentGenreDetailBinding
import com.hexa.arti.ui.search.adapter.ArtworkAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenreDetailFragment :
    BaseFragment<FragmentGenreDetailBinding>(R.layout.fragment_genre_detail) {

    private val args: GenreDetailFragmentArgs by navArgs()
    private val viewModel: GenreDetailViewModel by viewModels()

    val artAdapter = ArtworkAdapter {
        Log.d("확인", "클릭 확인")
    }


    override fun init() {
        initViews()
        initObserve()

        viewModel.getGenreRandomData(args.genreName)
    }

    private fun initObserve(){
        viewModel.resultArtwork.observe(viewLifecycleOwner){ resultArtworks ->
            artAdapter.submitList(resultArtworks)
        }
    }

    private fun initViews() {
        binding.rvArt.adapter = artAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}