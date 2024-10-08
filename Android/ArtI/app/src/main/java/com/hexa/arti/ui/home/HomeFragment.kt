package com.hexa.arti.ui.home

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentHomeBinding
import com.hexa.arti.ui.MainActivityViewModel
import com.hexa.arti.ui.home.adapter.ViewpageAdapter
import com.hexa.arti.util.LoadingRecommendDialog
import com.hexa.arti.util.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private var isShowDialog = false
    private var loadingDialog: LoadingRecommendDialog? = null

    private val viewModel: HomeViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()


    private val viewpageAdapter = ViewpageAdapter(
        onPlayClick = { item ->
            val action =
                HomeFragmentDirections.actionHomeFragmentToArtGalleryDetailFragment(item.homeGallery.galleryId)
            navigate(action)
        },
        onSliding = {
            binding.viewpager2.isUserInputEnabled = false
        },
        onNormal = {
            binding.viewpager2.isUserInputEnabled = true
        },
    )

    override fun init() {
        initAdapter()
        initObserve()
        initViews()
        initUserData()
    }

    private fun initUserData() {
        CoroutineScope(Dispatchers.Main).launch {
            if (viewModel.resultGalleries.value == null) {
                showLoadingDialog()
                mainActivityViewModel.getLoginData().collect { userData ->
                    userData?.let {
                        viewModel.getRecommendGalleries(userData.memberId)
                    }
                }
            }
        }
    }

    private fun initObserve() {
        viewModel.resultGalleries.observe(viewLifecycleOwner) {
            viewpageAdapter.submitList(it)
            hideLoadingDialog()
        }

    }

    private fun initAdapter() {
        binding.viewpager2.adapter = viewpageAdapter

    }

    override fun onPause() {
        super.onPause()
        loadingDialog?.dismiss()
    }

    private fun initViews() {

    }


    fun showLoadingDialog() {
        Log.d("확인", "이거호출")
        if (!isShowDialog) {
            isShowDialog = true
            loadingDialog = LoadingRecommendDialog()
            loadingDialog!!.isCancelable = false
            loadingDialog!!.show(mainActivity.supportFragmentManager, "loading")
        }
    }

    fun hideLoadingDialog() {
        Log.d("확인", "이거호출2")
        if (isShowDialog) {
            isShowDialog = false
            loadingDialog?.dismiss()
        }
    }


}