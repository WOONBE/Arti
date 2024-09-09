package com.hexa.arti.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentMyPageBinding
import com.hexa.arti.util.navigate


class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page){
    override fun init() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.portfolioBtn.setOnClickListener {
            navigate(R.id.action_myPageFragment_to_portfolioFragment)
        }
    }
}
