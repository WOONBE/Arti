package com.hexa.arti.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentMyPageBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.ui.setting.SettingFragmentDirections
import com.hexa.arti.util.navigate


class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page){

    override fun init() {
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){

            // 프로필 편집
            myPageProfileModifyTv.setOnClickListener {
                val action = SettingFragmentDirections.actionSettingFragmentToProfileDetailFragment(0)
                navigate(action)
            }
            // 비밀번호 수정
            myPagePassTv.setOnClickListener {
                val action = SettingFragmentDirections.actionSettingFragmentToProfileDetailFragment(1)
                navigate(action)
            }
            // 이용약관
            myPageInfoTv.setOnClickListener {
                val action = SettingFragmentDirections.actionSettingFragmentToProfileDetailFragment(2)
                navigate(action)
            }
            // 로그아웃
            myPageLogoutTv.setOnClickListener { mainActivity.moveLogin() }
            // 회원 탈퇴
            myPageDeleteTv.setOnClickListener { mainActivity.moveLogin() }

        }

    }
}
