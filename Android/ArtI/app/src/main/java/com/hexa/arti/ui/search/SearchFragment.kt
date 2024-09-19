package com.hexa.arti.ui.search

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.search.Art
import com.hexa.arti.data.model.search.ArtMuseum
import com.hexa.arti.data.model.search.Artist
import com.hexa.arti.databinding.FragmentSearchBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.ui.search.adapter.ArtAdapter
import com.hexa.arti.ui.search.adapter.ArtMuseumAdapter
import com.hexa.arti.ui.search.adapter.ArtistAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private var isSearchDetail = false

    private val viewModel: SearchViewModel by viewModels()

    private val artMuseumAdapter = ArtMuseumAdapter {
        Log.d("확인", "미술관 아이템 클릭")
    }
    private val artAdapter = ArtAdapter {
        Log.d("확인", "작품 아이템 클릭")
    }
    private val artistAdapter = ArtistAdapter {
        Log.d("확인", "작가 아이템 클릭")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        if(!isSearchDetail) mainActivity.hideBottomNav(false)

    }

    override fun init() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isSearchDetail) {
                        offSearchFocus()
                        isSearchDetail = false
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })

        initAdapters()
        initViews()
        initUIState()
    }

    private fun initUIState() {
        viewModel.artistResult.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.tvNoResultArtist.visibility = View.VISIBLE
            } else {
                binding.tvNoResultArtist.visibility = View.GONE
            }
            artistAdapter.submitList(it)
        }
    }

    private fun initAdapters() {
        binding.rvArtMuseumResult.adapter = artMuseumAdapter
        binding.rvArtResult.adapter = artAdapter
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
            Art(1, "1"),
            Art(2, "1"),
            Art(3, "1"),
        )

        val mockArtistData = listOf(
            Artist(artistId = 0, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 1, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 2, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 3, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 4, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 5, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
            Artist(artistId = 6, korName = "잠만보1", engName = "aa", imageUrl = "sad"),
        )

        artMuseumAdapter.submitList(mockArtMuseumData)
        artAdapter.submitList(mockArtData)
        artistAdapter.submitList(mockArtistData)
    }

    private fun initViews() {
        binding.tietSearch.setOnFocusChangeListener { _, hasFocus ->
            viewLifecycleOwner.lifecycleScope.launch {
                if (hasFocus) {
                    isSearchDetail = true
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
                viewModel.getArtistByString(keyword)

                binding.clSearchResult.visibility = View.VISIBLE
                isSearchDetail = true

                binding.tilSearch.clearFocus()
                mainActivity.hideBottomNav(true)
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

}