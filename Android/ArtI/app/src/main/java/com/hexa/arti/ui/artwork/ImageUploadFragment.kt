package com.hexa.arti.ui.artwork

import android.animation.Animator
import android.animation.ValueAnimator
import android.net.Uri
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentImageUploadBinding
import com.hexa.arti.util.handleImage
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody

private const val TAG = "ImageUploadFragment"

@AndroidEntryPoint
class ImageUploadFragment :
    BaseFragment<FragmentImageUploadBinding>(R.layout.fragment_image_upload) {

    private val args: ImageUploadFragmentArgs by navArgs()
    private val imageUploadViewModel: ImageUploadViewModel by viewModels()
    private var animator: Animator? = null

    private lateinit var image: MultipartBody.Part
    private var uri = ""
    override fun init() {

        val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        if (mainActivity.isUp) startProgressBarAnimation(60, 80)
        else startProgressBarAnimation(100, 80)
        mainActivity.isUp = false
        mainActivity.isDoubleUp = true

        with(binding) {

            artworkCreateImageTv.startAnimation(fadeInAnimation)
            sourceImg.startAnimation(fadeInAnimation)
            plusImg.startAnimation(fadeInAnimation)
            originImg.startAnimation(fadeInAnimation)
            artworkCreateImageBtn.startAnimation(fadeInAnimation)

            imageUploadViewModel.getArtWork(args.artId)
            imageUploadViewModel.artworkResult.observe(viewLifecycleOwner) {
                Glide.with(requireContext()).load(it.imageUrl).into(originImg)
            }

            artworkBackBtn.setOnClickListener {
                popBackStack()
            }
            artworkCreateImageBtn.setOnClickListener {

                if (uri == "") {
                    makeToast("합성할 사진을 골라주세요")
                    return@setOnClickListener
                } else {
                    mainActivity.showLoadingDialog()
                    Log.d(TAG, "init: ${image}")
                    imageUploadViewModel.makeImage(image, args.artId)
                }
            }
            sourceImgCv.setOnClickListener {
                openImagePicker()
            }
        }

        imageUploadViewModel.imageResponse.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mainActivity.hideLoadingDialog()
                mainActivity.isUp = true
                mainActivity.isDoubleUp = false
                val action =
                    ImageUploadFragmentDirections.actionImageUploadFragmentToArtworkResultFragment(
                        it,
                        1
                    )
                navigate(action)
                imageUploadViewModel.updateImageResponse()
            }
        }
    }

    private val getImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                image = handleImage(it, requireContext(), name = "content_image")
                binding.sourceImg.setImageURI(it)
                this.uri = it.toString()
            }
        }

    private fun openImagePicker() {
        getImageLauncher.launch("image/*")
    }

    private fun startProgressBarAnimation(start: Int, end: Int) {
        animator = ValueAnimator.ofInt(start, end).apply {
            duration = 800
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Int
                binding.progressBar.progress = progress
            }
        }
        animator?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        animator?.cancel()
    }
}