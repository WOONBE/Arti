package com.hexa.arti.ui.artwork

import androidx.fragment.app.Fragment
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentImageUploadBinding
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack

class ImageUploadFragment : BaseFragment<FragmentImageUploadBinding>(R.layout.fragment_image_upload){
    override fun init() {
        with(binding){
            artworkBackBtn.setOnClickListener {
                popBackStack()
            }
            artworkCreateImageBtn.setOnClickListener {
                navigate(R.id.action_imageUploadFragment_to_artworkResultFragment)
            }
        }
    }

}