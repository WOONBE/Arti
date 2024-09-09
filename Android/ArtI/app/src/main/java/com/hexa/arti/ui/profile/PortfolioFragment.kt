package com.hexa.arti.ui.profile

import android.os.Bundle
import android.view.View
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentPortfolioBinding
import com.hexa.arti.util.navigate

class PortfolioFragment : BaseFragment<FragmentPortfolioBinding>(R.layout.fragment_portfolio) {
    override fun init() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mypageBtn.setOnClickListener {
            navigate(R.id.action_portfolioFragment_to_myPageFragment)
        }
    }
}