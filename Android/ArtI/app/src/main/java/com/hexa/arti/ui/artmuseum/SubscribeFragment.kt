package com.hexa.arti.ui.artmuseum

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artmuseum.GalleryBanner
import com.hexa.arti.databinding.FragmentSubscribeBinding
import com.hexa.arti.ui.MainActivityViewModel
import com.hexa.arti.ui.artmuseum.adpater.SubScribeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscribeFragment : BaseFragment<FragmentSubscribeBinding>(R.layout.fragment_subscribe) {

    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var adapter: SubScribeAdapter
    override fun init() {
        adapter = SubScribeAdapter(requireContext(), onClick = {
            Log.d("", "init: $it")

            findNavController().navigate(
                MyGalleryHomeFragmentDirections.actionMyGalleryHomeFragmentToArtMuseumFragment(
                    GalleryBanner(
                        galleryId = it.galleryId,
                        name = it.name,
                        description = it.galleryDescription ?: "안녕",
                        imageUrl = it.profileImageResId,
                        viewCount = it.viewCount,
                        ownerId = 0
                    )
                )
            )
        })
        binding.subscribeRecyclerview.adapter = adapter

        mainActivityViewModel.subscriptionGallery.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        initObserve()
    }

    private fun initObserve(){
        mainActivityViewModel.fragmentState.observe(viewLifecycleOwner) { state ->
            if (state == MainActivityViewModel.SUBSCRIBE_FRAGMENT) {
                mainActivityViewModel.getSubscriptionGalleries(mainActivityViewModel.userId)
            }
        }
    }

}