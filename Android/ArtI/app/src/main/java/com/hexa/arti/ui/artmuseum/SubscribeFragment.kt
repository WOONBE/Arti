package com.hexa.arti.ui.artmuseum

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artmuseum.Subscriber
import com.hexa.arti.databinding.FragmentSubscribeBinding
import com.hexa.arti.ui.MainActivityViewModel
import com.hexa.arti.ui.artmuseum.adpater.SubScribeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscribeFragment : BaseFragment<FragmentSubscribeBinding>(R.layout.fragment_subscribe) {

    private val mainActivityViewModel : MainActivityViewModel by activityViewModels()
    private val subscribeViewModel : SubscribeViewModel by viewModels()
    private var userId : Int = 0
    private lateinit var adapter: SubScribeAdapter
    override fun init() {
        lifecycleScope.launch {

            mainActivityViewModel.getLoginData().collect { d ->
                Log.d("확인", "onCreate: ${d?.galleryId}")
                d?.let {
                    userId = d.memberId
                }
                subscribeViewModel.getSubscriptionGalleries(userId)

            }
        }

        adapter= SubScribeAdapter(requireContext(), onClick = {
            Log.d("", "init: $it")
        })
        binding.subscribeRecyclerview.adapter = adapter

        subscribeViewModel.subscriptionGallery.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

}