package com.hexa.arti.ui.artwork

import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentIsSelectCreateImageBinding
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack

class IsSelectCreateImageFragment : BaseFragment<FragmentIsSelectCreateImageBinding>(R.layout.fragment_is_select_create_image) {
    override fun init(){
        with(binding){
            //뒤로가기
            artworkBackBtn.setOnClickListener {
                popBackStack()
            }

            //네
            artworkCreateBtn.setOnClickListener {
                navigate(R.id.action_isSelectCreateImageFragment_to_imageUploadFragment)
            }
            // 아니오
            artworkMaintainBtn.setOnClickListener {

            }
        }

    }

}