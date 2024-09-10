package com.hexa.arti.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentProfileDetailBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.util.popBackStack


class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>(R.layout.fragment_profile_detail) {
    private lateinit var mainActivity: MainActivity
    private val ars : ProfileDetailFragmentArgs by navArgs()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun init() {
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
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
            }
        }
    }
    private fun initEvent(){
        with(binding){
            profileDetailBackBtn.setOnClickListener { popBackStack() }
            profilePassBackBtn.setOnClickListener { popBackStack() }
            profileDetailCancelBtn.setOnClickListener { popBackStack() }
            profilePassCancelBtn.setOnClickListener { popBackStack() }

            // 프로필 수정
            profileDetailModifyBtn.setOnClickListener { popBackStack() }
            // 비밀번호 수정
            profilePassModifyBtn.setOnClickListener { popBackStack() }
        }
    }
}