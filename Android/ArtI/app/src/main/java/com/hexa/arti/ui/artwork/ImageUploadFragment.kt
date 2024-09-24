package com.hexa.arti.ui.artwork

import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentImageUploadBinding
import com.hexa.arti.util.LoadingDialog
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

    private lateinit var image: MultipartBody.Part
    private var uri = ""
    override fun init() {
        with(binding) {
            artworkBackBtn.setOnClickListener {
                popBackStack()
            }
            artworkCreateImageBtn.setOnClickListener {
                val action = ImageUploadFragmentDirections.actionImageUploadFragmentToArtworkResultFragment("a",1)
                navigate(action)
//                if (uri == "") {
//                    makeToast("합성할 사진을 골라주세요")
//                    return@setOnClickListener
//                } else {
//                    mainActivity.showLoadingDialog()
//                    Log.d(TAG, "init: ${image}")
//                    imageUploadViewModel.makeImage(image, args.artId)
//                }
//                navigate(R.id.action_imageUploadFragment_to_artworkResultFragment)
            }
            sourceImgCv.setOnClickListener {
                openImagePicker()
            }
        }

        imageUploadViewModel.imageResponse.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mainActivity.hideLoadingDialog()
                val action = ImageUploadFragmentDirections.actionImageUploadFragmentToArtworkResultFragment(it,1)
                navigate(action)
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

}