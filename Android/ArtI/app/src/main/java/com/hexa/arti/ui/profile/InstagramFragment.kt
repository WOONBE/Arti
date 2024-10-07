package com.hexa.arti.ui.profile

import android.webkit.WebViewClient
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentInstagramBinding

class InstagramFragment : BaseFragment<FragmentInstagramBinding>(R.layout.fragment_instagram) {

    override fun init() {
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl("http://j11d106.p.ssafy.io/instagram/redirect")
        }
    }
}