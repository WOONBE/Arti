package com.hexa.arti.ui.search.artwork

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtBannerBinding
import com.hexa.arti.ui.MainActivityViewModel
import com.hexa.arti.ui.search.adapter.ArtworkAdapter
import com.hexa.arti.util.LoadingDialog
import com.hexa.arti.util.LoadingRecommendDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtBannerFragment : BaseFragment<FragmentArtBannerBinding>(R.layout.fragment_art_banner) {

    private var isShowDialog = false
    private lateinit var loadingDialog: LoadingRecommendDialog

    private val viewModel: ArtBannerViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    val artAdapter = ArtworkAdapter {
        Log.d("확인", "작품 클릭 확인")
    }


    override fun init() {
        binding.rvArt.adapter = artAdapter

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        showLoadingDialog()
        initObserve()
        initUserData()
    }

    private fun initUserData() {
        CoroutineScope(Dispatchers.Main).launch {
            mainActivityViewModel.getLoginData().collect { userData ->
                userData?.let {
                    viewModel.getRecommendArtworks(userData.memberId)
                }
            }
        }
    }

    private fun initObserve() {
        viewModel.resultArtworks.observe(viewLifecycleOwner) {
            artAdapter.submitList(it)
            hideLoadingDialog()
        }
    }

    fun showLoadingDialog() {
        Log.d("확인","이거호출")
        if(!isShowDialog){
            isShowDialog = true
            loadingDialog = LoadingRecommendDialog()
            loadingDialog.isCancelable = false
            loadingDialog.show(mainActivity.supportFragmentManager, "loading")
        }
    }

    fun hideLoadingDialog() {
        Log.d("확인","이거호출2")
        if(isShowDialog){
            isShowDialog = false
            loadingDialog.dismiss()
        }
    }
}