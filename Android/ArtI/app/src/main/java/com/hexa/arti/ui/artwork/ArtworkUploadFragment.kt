package com.hexa.arti.ui.artwork

import android.animation.ValueAnimator
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
        if (mainActivity.isUp) {
            startProgressBarAnimation(0, 20)
        }else {
            startProgressBarAnimation(40, 20)
        }
        with(binding) {
            artworkBackBtn.setOnClickListener {
                popBackStack()
            }
            artworkNextBtn.setOnClickListener {
                mainActivity.isUp = true
                navigate(R.id.action_artworkUploadFragment_to_selectArtworkFragment)
            }

            artworkCommentTv.startAnimation(fadeInAnimation)
            artworkNextBtn.startAnimation(fadeInAnimation)
        }

    }

    private fun startProgressBarAnimation(start: Int, end: Int) {
        val animator = ValueAnimator.ofInt(start, end).apply {
            duration = 800
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Int
                binding.progressBar.progress = progress
            }
        }
        animator.start()
    }

}