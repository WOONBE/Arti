package com.hexa.arti.ui.search

import com.bumptech.glide.Glide
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.home.ArtTheme
import com.hexa.arti.data.model.search.PreviewImage
import com.hexa.arti.databinding.FragmentArtMuseumBinding
import com.hexa.arti.ui.home.adapter.ThemeAdapter
import com.hexa.arti.ui.search.adapter.PreviewAdapter
import kotlin.random.Random

class ArtMuseumFragment : BaseFragment<FragmentArtMuseumBinding>(R.layout.fragment_art_museum) {

    private val previewAdapter = PreviewAdapter { clickedImage ->
        changeFocusItem(clickedImage)
    }

    private val themeAdapter = ThemeAdapter()

    override fun init() {
        initViews()
    }

    private fun initViews() {

        val mockUrl = listOf(
            "https://upload.wikimedia.org/wikipedia/commons/8/87/2012_Henrique_Matos_Optical_Motion_01.jpg",
            "https://i0.wp.com/blog.rightbrain.co.kr/wp-content/uploads/2014/11/victorvasarely_02.jpg?fit=408%2C328&ssl=1",
            "https://cdn.knupresscenter.com/news/photo/201103/14901_1621_4915.jpg",
            "https://cdn.knupresscenter.com/news/photo/201103/14901_1622_4915.jpg",
            "https://cicamuseum.com/wp-content/uploads/2014/03/semikim_01.jpg",
        )

        binding.rvPreview.apply {
            adapter = previewAdapter
            val previewMockData = mutableListOf<PreviewImage>()
            for (i in 0..20) {
                previewMockData.add(PreviewImage(id = i, url = mockUrl[Random.nextInt(5)]))
            }
            previewMockData[0] = previewMockData[0].copy(isFocus = true)
            Glide.with(requireContext())
                .load(previewMockData[0].url)
                .error(R.drawable.gallery_example)
                .into(binding.ivArtImage)

            previewAdapter.submitList(previewMockData)
        }

        binding.rvTheme.apply {
            adapter = themeAdapter
            val artThemeList = listOf(
                ArtTheme("절망"),
                ArtTheme("희망"),
                ArtTheme("병현")
            )

            themeAdapter.submitList(artThemeList)
        }

        
    }

    private fun changeFocusItem(clickedImage: PreviewImage) {
        val updateList = previewAdapter.currentList.map { previewImage ->
            previewImage.copy(isFocus = (previewImage.id == clickedImage.id))
        }

        Glide.with(requireContext())
            .load(clickedImage.url)
            .error(R.drawable.gallery_sample2)
            .into(binding.ivArtImage)

        previewAdapter.submitList(updateList)
    }

}