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
import com.hexa.arti.util.handleImage
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
                handleImage(it,requireContext())
                binding.myPageProfileIv.setImageURI(it)
            }
        }
    private fun openImagePicker() {
        getImageLauncher.launch("image/*")
    }




}