package com.hexa.arti.ui.search

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.data.model.search.ArtMuseum
import com.hexa.arti.data.model.search.Artist
import com.hexa.arti.databinding.FragmentSearchBinding
import com.hexa.arti.ui.search.adapter.ArtMuseumAdapter
import com.hexa.arti.ui.search.adapter.ArtistAdapter
import com.hexa.arti.ui.search.adapter.ArtworkAdapter
import com.hexa.arti.ui.search.paging.ArtworkPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()

    private val artMuseumAdapter = ArtMuseumAdapter {
        Log.d("확인", "미술관 아이템 클릭")
    }

    private val artAdapter = ArtworkAdapter {
        Log.d("확인", "작품 아이템 클릭")
    }

    private val artworkPagingAdapter = ArtworkPagingAdapter {
        Log.d("확인", "페이징 아이템 클릭")
    }

    private val artistAdapter = ArtistAdapter { artist ->
        moveToArtistDetailFragment(artist)
    }

    override fun init() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.state != BASE_STATE) {
                        offSearchFocus()
                        viewModel.state = BASE_STATE
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })
        initUIState()
        initAdapters()
        initViews()

        checkState()
    }

    private fun checkState() {
        if (viewModel.state == BASE_STATE) mainActivity.hideBottomNav(false)
        if (viewModel.state == RESULT_STATE) {
            binding.clSearchResult.visibility = View.VISIBLE
            binding.clSearchBanner.visibility = View.GONE
            mainActivity.hideBottomNav(true)
        }
    }

    private fun initUIState() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.artWorkResult.collect { pagingData ->
                    artworkPagingAdapter.submitData(pagingData)
                }
            }
        }

        viewModel.artistResult.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.tvNoResultArtist.visibility = View.VISIBLE
                updateConstraintArtist()
            } else {
                binding.tvNoResultArtist.visibility = View.GONE
            }
            artistAdapter.submitList(it)
        }

    }

    private fun updateConstraintArtist() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clSearchResult)

        constraintSet.connect(
            R.id.tv_artist_result,
            ConstraintSet.TOP,
            R.id.tv_no_result_artwork,
            ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(binding.clSearchResult)
    }

    private fun initAdapters() {
        binding.rvArtMuseumResult.adapter = artMuseumAdapter
        binding.rvArtResult.adapter = artworkPagingAdapter
        binding.rvArtistResult.adapter = artistAdapter
        val layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        binding.rvArtistResult.layoutManager = layoutManager

        val mockArtMuseumData = listOf(
            ArtMuseum(1, "1", "1"),
            ArtMuseum(2, "1", "1"),
            ArtMuseum(3, "1", "1"),
        )

        val mockArtData = listOf(
            Artwork(1, "1","","",""),
            Artwork(2, "1","","",""),
            Artwork(3, "1","","",""),
        )

        artMuseumAdapter.submitList(mockArtMuseumData)
//        artAdapter.submitList(mockArtData)
    }

    private fun initViews() {
        binding.tietSearch.setOnFocusChangeListener { _, hasFocus ->
            viewLifecycleOwner.lifecycleScope.launch {
                if (hasFocus) {
                    viewModel.state = SEARCH_STATE
                    onSearchFocus()
                }
            }
        }

        binding.tvCancel.setOnClickListener {
            offSearchFocus()
        }

        binding.ivClearText.setOnClickListener {
            binding.tietSearch.setText("")
        }

        binding.tietSearch.addTextChangedListener { text ->
            if (text.toString().isEmpty()) binding.ivClearText.visibility = View.GONE
            else binding.ivClearText.visibility = View.VISIBLE
        }

        binding.tietSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)

                val keyword = v.text.toString()

//                viewModel.getArtworkByString(keyword)

                viewModel.getArtworkByString(keyword)
                viewModel.getArtistByString(keyword)

                viewModel.state = RESULT_STATE

                binding.clSearchResult.visibility = View.VISIBLE
                mainActivity.hideBottomNav(true)

                binding.tilSearch.clearFocus()
                return@OnEditorActionListener true
            }

            false
        })

        binding.ivBannerArt.setOnClickListener {
            moveToArtBannerFragment()
        }

        binding.ivGradationArt.setOnClickListener {
            moveToArtBannerFragment()
        }

        binding.tvArt.setOnClickListener {
            moveToArtBannerFragment()
        }

        binding.ivBannerArtist.setOnClickListener {
            moveToArtistBannerFragment()
        }

        binding.ivGradationArtist.setOnClickListener {
            moveToArtistBannerFragment()
        }

        binding.tvArtist.setOnClickListener {
            moveToArtistBannerFragment()
        }

        binding.ivBannerGenre.setOnClickListener {
            moveToGenreBannerFragment()
        }

        binding.ivGradationGenre.setOnClickListener {
            moveToGenreBannerFragment()
        }

        binding.tvGenre.setOnClickListener {
            moveToGenreBannerFragment()
        }


        binding.ivBannerArtMuseum.setOnClickListener {
            moveToArtMuseumFragment()
        }

        binding.ivGradationArtMuseum.setOnClickListener {
            moveToArtMuseumFragment()
        }

        binding.tvArtMuseum.setOnClickListener {
            moveToArtMuseumFragment()
        }
    }

    private fun onSearchFocus() {

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.tietSearch, InputMethodManager.SHOW_IMPLICIT)

        binding.tvSearchTitle.visibility = View.GONE
        binding.tvCancel.visibility = View.VISIBLE

        binding.clSearchBanner.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction {
                binding.clSearchBanner.visibility = View.GONE
            }

        binding.clSearchBottom.alpha = 0f
        binding.clSearchBottom.visibility = View.VISIBLE
        binding.clSearchBottom.animate()
            .alpha(1f)
            .setDuration(150)

        binding.clSearchResult.visibility = View.GONE
    }

    private fun offSearchFocus() {

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

        binding.tvSearchTitle.visibility = View.VISIBLE
        binding.tvCancel.visibility = View.GONE

        binding.tietSearch.setText("")
        binding.tietSearch.clearFocus()

        binding.clSearchBottom.visibility = View.GONE
        binding.clSearchResult.visibility = View.GONE

        binding.clSearchBanner.alpha = 0f
        binding.clSearchBanner.visibility = View.VISIBLE
        binding.clSearchBanner.animate()
            .alpha(1f)
            .setDuration(150)

        mainActivity.hideBottomNav(false)
    }

    private fun moveToArtBannerFragment() {
        findNavController().navigate(R.id.action_searchFragment_to_artBannerFragment)
    }

    private fun moveToArtistBannerFragment() {
        findNavController().navigate(R.id.action_searchFragment_to_artistBannerFragment)
    }

    private fun moveToGenreBannerFragment() {
        findNavController().navigate(R.id.action_searchFragment_to_genreBannerFragment)
    }

    private fun moveToArtMuseumFragment() {
        findNavController().navigate(R.id.action_searchFragment_to_artMuseumBannerFragment)
    }

    private fun moveToArtistDetailFragment(artist: Artist) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToArtistDetailFragment(
                artist
            )
        )
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.state == BASE_STATE) mainActivity.hideBottomNav(false)

    }


    companion object {
        const val BASE_STATE = 1
        const val SEARCH_STATE = 2
        const val RESULT_STATE = 3
    }
}