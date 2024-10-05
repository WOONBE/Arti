package com.hexa.arti.ui.search.museum

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artmuseum.GalleryBanner
import com.hexa.arti.data.model.artmuseum.ThemeArtwork
import com.hexa.arti.databinding.FragmentArtMuseumBinding
import com.hexa.arti.ui.home.adapter.ThemeAdapter
import com.hexa.arti.ui.search.adapter.PreviewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtMuseumFragment : BaseFragment<FragmentArtMuseumBinding>(R.layout.fragment_art_museum) {

    private val viewModel: ArtMuseumViewModel by viewModels()
    private val args: ArtMuseumFragmentArgs by navArgs()
    private val previewAdapter = PreviewAdapter { clickedImage ->
        changeFocusItem(clickedImage)
    }
    private val themeAdapter = ThemeAdapter()

    override fun init() {
        Log.d("확인","갤러리 ID ${args.gallery.galleryId}")
        initObserve()
        initViews(args.gallery)
    }

    private fun initObserve() {
        viewModel.resultTotalTheme.observe(viewLifecycleOwner) { resultTotalTheme ->
            if (resultTotalTheme.isEmpty()) {
                Log.d("확인", "통신에러")
            } else {
                //리스트 다 합쳐서 artwork 있나 확인
                val totalArtworks = mutableListOf<ThemeArtwork>()
                for (theme in resultTotalTheme) {
                    for (artwork in theme.artworks) {
                        totalArtworks.add(artwork)
                    }
                }
                if (totalArtworks.isEmpty()) {
                    binding.clTotalMuseum.visibility = View.GONE
                    binding.tvNoArtworks.visibility = View.VISIBLE
                } else {
                    Log.d("확인", "$totalArtworks")
                    previewAdapter.submitList(totalArtworks)
                }
            }
        }
    }

    private fun initViews(gallery: GalleryBanner) {
        binding.rvPreview.adapter = previewAdapter

        binding.rvTheme.adapter = themeAdapter

        viewModel.getTotalThemes(gallery.galleryId)

        binding.ivRight.setOnClickListener {
            val nextFocusedIndex = previewAdapter.currentList.indexOfFirst { it.isFocus } + 1

            if (nextFocusedIndex >= previewAdapter.currentList.size) return@setOnClickListener

            changeFocusItem(previewAdapter.currentList[nextFocusedIndex])

            binding.rvPreview.layoutManager?.smoothScrollToPosition(
                binding.rvPreview,
                RecyclerView.State(),
                nextFocusedIndex
            )
        }

        binding.ivLeft.setOnClickListener {

            val prevFocusedIndex = previewAdapter.currentList.indexOfFirst { it.isFocus } - 1

            if (prevFocusedIndex < 0) return@setOnClickListener

            changeFocusItem(previewAdapter.currentList[prevFocusedIndex])

            binding.rvPreview.layoutManager?.smoothScrollToPosition(
                binding.rvPreview,
                RecyclerView.State(),
                prevFocusedIndex
            )
        }

        binding.tvMuseumTitle.text = gallery.name
        binding.tvIntroduceContent.text = gallery.description
    }

    private fun changeFocusItem(clickedImage: ThemeArtwork) {
        val updateList = previewAdapter.currentList.map { previewImage ->
            previewImage.copy(isFocus = (previewImage.id == clickedImage.id))
        }

        Glide.with(requireContext())
            .load(clickedImage.imageUrl)
            .error(R.drawable.gallery_sample2)
            .into(binding.ivArtImage)

        previewAdapter.submitList(updateList)
    }


}