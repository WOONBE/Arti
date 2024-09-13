package com.hexa.arti.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentProfileDetailBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.util.popBackStack
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>(R.layout.fragment_profile_detail) {


    private val ars : ProfileDetailFragmentArgs by navArgs()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun init() {
        initView()
        initEvent()
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }


    private fun initView(){
        with(binding){
            when(ars.detailType){
                0-> {
                    profileDetailModifyCl.visibility = View.VISIBLE
                }
                1->{
                    profilePassModifyCl.visibility = View.VISIBLE
                }
                2->{
                    profileInfoCl.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun initEvent(){
        with(binding){
            profileDetailBackBtn.setOnClickListener { popBackStack() }
            profilePassBackBtn.setOnClickListener { popBackStack() }
            profileDetailCancelBtn.setOnClickListener { popBackStack() }
            profilePassCancelBtn.setOnClickListener { popBackStack() }
            profileInfoBackBtn.setOnClickListener { popBackStack() }
            // 프로필 수정
            profileDetailModifyBtn.setOnClickListener { popBackStack() }
            // 비밀번호 수정
            profilePassModifyBtn.setOnClickListener { popBackStack() }

            myPageProfileIv.setOnClickListener {
                openImagePicker()
            }
        }
    }


    private val getImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                handleImage(it)
                binding.myPageProfileIv.setImageURI(it)
            }
        }
    private fun openImagePicker() {
        getImageLauncher.launch("image/*")
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val contentResolver = context.contentResolver
        val file =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg")

        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
            }
        }
        return file
    }

    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val compressedFile = File(file.parent, "compressed_${file.name}")
        FileOutputStream(compressedFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // 80% 압축 품질
        }
        return compressedFile
    }

    private fun handleImage(imageUri: Uri) {
        var file = uriToFile(requireContext(), imageUri)

        val maxSize = 10 * 1024 * 1024 // 10MB
        if (file.length() > maxSize) {
            file = compressImage(file)

            if (file.length() > maxSize) {
                makeToast("File size still exceeds limit after compression")
                return
            }
        }
        val requestFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
    }
}