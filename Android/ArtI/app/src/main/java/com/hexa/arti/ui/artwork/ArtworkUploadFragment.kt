package com.hexa.arti.ui.artwork

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtworkUploadBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack

class ArtworkUploadFragment : BaseFragment<FragmentArtworkUploadBinding>(R.layout.fragment_artwork_upload) {

    override fun init() {
        with(binding){
            artworkBackBtn.setOnClickListener {
                popBackStack()
            }
            artworkNextBtn.setOnClickListener {
                navigate(R.id.action_artworkUploadFragment_to_selectArtworkFragment)
            }
        }
    }

}