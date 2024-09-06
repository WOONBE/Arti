package com.hexa.arti.ui.artGalleryDetail

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtDetailBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.util.popBackStack

class ArtDetailFragment : BaseFragment<FragmentArtDetailBinding>(R.layout.fragment_art_detail) {
    override fun init() {
    }
    private lateinit var mainActivity : MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.artDetailCancelBtn.setOnClickListener {
            popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity.changeLandScope()
        mainActivity.hideBottomNav(true)
    }

}