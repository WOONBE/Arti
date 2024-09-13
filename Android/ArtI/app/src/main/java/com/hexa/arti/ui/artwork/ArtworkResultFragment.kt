package com.hexa.arti.ui.artwork

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtworkResultBinding
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack

class ArtworkResultFragment : BaseFragment<FragmentArtworkResultBinding>(R.layout.fragment_artwork_result) {
    override fun init() {
        with(binding){
            artworkBackBtn.setOnClickListener {
                popBackStack()
            }
            artworkResultBtn.setOnClickListener {
                val action =ArtworkResultFragmentDirections.actionArtworkResultFragmentToMyGalleryHomeFragment2(1)
                navigate(action)
            }
        }
    }

}