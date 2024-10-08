package com.hexa.arti.ui.artwork

import android.view.animation.AnimationUtils
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtworkUploadBinding
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack

class ArtworkUploadFragment :
    BaseFragment<FragmentArtworkUploadBinding>(R.layout.fragment_artwork_upload) {

    override fun init() {
        val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.artwork_fade_in)
        with(binding) {
            artworkBackBtn.setOnClickListener {
                popBackStack()
            }
            artworkNextBtn.setOnClickListener {
                navigate(R.id.action_artworkUploadFragment_to_selectArtworkFragment)
            }

            artworkCommentTv.startAnimation(fadeInAnimation)
            artworkNextBtn.startAnimation(fadeInAnimation)
        }

    }

}