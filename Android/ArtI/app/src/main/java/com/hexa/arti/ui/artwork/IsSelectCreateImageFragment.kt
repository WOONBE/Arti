package com.hexa.arti.ui.artwork

import android.util.Log
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentIsSelectCreateImageBinding
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack

class IsSelectCreateImageFragment : BaseFragment<FragmentIsSelectCreateImageBinding>(R.layout.fragment_is_select_create_image) {

    private val args : IsSelectCreateImageFragmentArgs by navArgs()

    override fun init(){
        with(binding){
            //뒤로가기
            artworkBackBtn.setOnClickListener {
                popBackStack()
            }

            //네
            artworkCreateBtn.setOnClickListener {
                val action = IsSelectCreateImageFragmentDirections.actionIsSelectCreateImageFragmentToImageUploadFragment(args.artId)
                navigate(action)
            }
            // 아니오
            artworkMaintainBtn.setOnClickListener {
                Log.d("확인", "init: ${args.artId.toString()}")
                val action = IsSelectCreateImageFragmentDirections.actionIsSelectCreateImageFragmentToArtworkResultFragment(args.artId.toString(),0)
                navigate(action)
            }
        }

    }

}