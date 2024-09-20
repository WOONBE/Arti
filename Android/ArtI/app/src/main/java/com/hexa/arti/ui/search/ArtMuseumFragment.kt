package com.hexa.arti.ui.search

import android.util.Log
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.home.ArtTheme
import com.hexa.arti.data.model.search.PreviewImage
import com.hexa.arti.databinding.FragmentArtMuseumBinding
import com.hexa.arti.ui.home.adapter.ThemeAdapter
import com.hexa.arti.ui.search.adapter.PreviewAdapter

class ArtMuseumFragment : BaseFragment<FragmentArtMuseumBinding>(R.layout.fragment_art_museum) {

    private val previewAdapter = PreviewAdapter {
        Log.d("확인", "클릭 확인")
    }

    private val themeAdapter = ThemeAdapter()

    override fun init() {
        initViews()
    }

    private fun initViews() {

        binding.rvPreview.apply {
            adapter = previewAdapter
            val previewMockData = mutableListOf<PreviewImage>()
            for (i in 0..20) {
                previewMockData.add(PreviewImage(id = i, url = ""))
            }
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

}