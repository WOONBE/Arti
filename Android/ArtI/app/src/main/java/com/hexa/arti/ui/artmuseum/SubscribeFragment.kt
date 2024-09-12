package com.hexa.arti.ui.artmuseum

import android.os.Bundle
import android.view.View
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artmuseum.Subscriber
import com.hexa.arti.databinding.FragmentSubscribeBinding
import com.hexa.arti.ui.artmuseum.adpater.SubScribeAdapter
import com.hexa.arti.util.navigate

class SubscribeFragment : BaseFragment<FragmentSubscribeBinding>(R.layout.fragment_subscribe) {

    private val example = arrayListOf(
        Subscriber(
            profileImageResId = R.drawable.profile_img,
            name = "병현이의 미술관",
            username = "병현이"

        ), Subscriber(
            profileImageResId = R.drawable.profile_img,
            name = "병현이의 미술관",
            username = "병현이"

        ), Subscriber(
            profileImageResId = R.drawable.profile_img,
            name = "병현이의 미술관",
            username = "병현이"

        )
    )

    override fun init() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.subscribeRecyclerview.adapter = SubScribeAdapter().apply {
            submitList(example)
        }
        binding.myGalleryBtn.setOnClickListener{
            navigate(R.id.action_subscribeFragment_to_myGalleryFragment)
        }
    }
}