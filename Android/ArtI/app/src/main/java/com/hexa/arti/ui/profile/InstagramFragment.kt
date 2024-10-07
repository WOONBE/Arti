package com.hexa.arti.ui.profile

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentInstagramBinding

class InstagramFragment : BaseFragment<FragmentInstagramBinding>(R.layout.fragment_instagram) {

    override fun init() {

        binding.webView.settings.javaScriptEnabled = true

        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null && url.contains("callback?code")) {
                        makeToast("인증 성공하였습니다.")
                        Log.d("확인", "인증 성공 ${url}")
                        binding.webView.loadUrl("javascript:window.Android.processHTML(document.documentElement.outerHTML);")
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
            }
            loadUrl("http://j11d106.p.ssafy.io/instagram/redirect")
        }
    }

}