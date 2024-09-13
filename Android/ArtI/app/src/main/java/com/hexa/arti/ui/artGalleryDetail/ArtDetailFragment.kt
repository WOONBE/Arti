package com.hexa.arti.ui.artGalleryDetail

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtDetailBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.util.popBackStack

class ArtDetailFragment : BaseFragment<FragmentArtDetailBinding>(R.layout.fragment_art_detail) {
    override fun init() {
    }

    private val args: ArtDetailFragmentArgs by navArgs()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        binding.artDetailCancelBtn.setOnClickListener {
            popBackStack()
        }
        binding.artDetailBackBtn.setOnClickListener {
            binding.artDetailCl.visibility = View.VISIBLE
            binding.artDetailThemeCl.visibility = View.GONE
        }
        binding.artDetailSubmitBtn.setOnClickListener {
            binding.artDetailCl.visibility = View.VISIBLE
            binding.artDetailThemeCl.visibility = View.GONE
        }
        binding.artDetailSaveBtn.setOnClickListener {
            binding.artDetailCl.visibility = View.GONE
            binding.artDetailThemeCl.visibility = View.VISIBLE
        }
    }

    private fun initView(){
        binding.artDetailIv.setImageResource(args.imgId)
        val radioButtons = listOf(
            binding.radioButton1,
            binding.radioButton2,
            binding.radioButton3,
            binding.radioButton4,
            binding.radioButton5,
            binding.radioButton6
        )
        for (radioButton in radioButtons) {
            radioButton.setOnClickListener {
                // 클릭된 RadioButton만 선택되도록 하고, 나머지는 선택 해제
                for (rb in radioButtons) {
                    rb.isChecked = rb == radioButton
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity.changeLandScope()
        mainActivity.hideBottomNav(true)
    }

}