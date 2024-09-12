package com.hexa.arti.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtMuseumBinding

class ArtMuseumFragment : BaseFragment<FragmentArtMuseumBinding>(R.layout.fragment_art_museum) {
    override fun init() {

    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

}