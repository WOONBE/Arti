package com.hexa.arti.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentPortfolioBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.util.navigate

class PortfolioFragment : BaseFragment<FragmentPortfolioBinding>(R.layout.fragment_portfolio) {
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun init() {
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}