package com.hexa.arti.ui.artwork

import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.databinding.FragmentSelectArtworkBinding
import com.hexa.arti.ui.artwork.adapter.SelectArtworkAdapter
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack

class SelectArtworkFragment :
    BaseFragment<FragmentSelectArtworkBinding>(R.layout.fragment_select_artwork) {
    private lateinit var adapter: SelectArtworkAdapter

    private val tempList = arrayListOf<Artwork>(
        Artwork(1, "",""),
        Artwork(2, "",""),
        Artwork(3, "",""),
        Artwork(4, "",""),
        Artwork(5, "",""),
        Artwork(6, "",""),

    )

    override fun init() {
        adapter = SelectArtworkAdapter(onClick = { id ->
            navigate(R.id.action_selectArtworkFragment_to_isSelectCreateImageFragment)

        }
        )
        with(binding) {

            selectArtworkRv.adapter = adapter
            adapter.submitList(tempList)

            artworkBackBtn.setOnClickListener { popBackStack() }
        }
    }

}