package com.hexa.arti.ui.artmuseum

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.GridLayout
import android.widget.PopupMenu
import androidx.core.animation.doOnEnd
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.MyGalleryThemeItem
import com.hexa.arti.databinding.FragmentMyGalleryBinding
import com.hexa.arti.ui.artmuseum.adpater.MyGalleryThemeAdapter
import com.hexa.arti.util.navigate


class MyGalleryFragment : BaseFragment<FragmentMyGalleryBinding>(R.layout.fragment_my_gallery){
    override fun init() {
    }
    private lateinit var adapter: MyGalleryThemeAdapter
    private  val sampleData = listOf(
        MyGalleryThemeItem("병현이의 절망", listOf(R.drawable.survey_example, R.drawable.survey_example, R.drawable.survey_example)),
        MyGalleryThemeItem("미술관 테마2", listOf(R.drawable.survey_example, R.drawable.survey_example,R.drawable.survey_example))
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.subscribeBtn.setOnClickListener {
            navigate(R.id.action_myGalleryFragment_to_subscribeFragment)
        }
        adapter = MyGalleryThemeAdapter()
        binding.myGalleryThemeRv.adapter = adapter
        adapter.submitList(sampleData)

        binding.myGalleryNameModifyBtn.setOnClickListener {
            // 제목 버전
            binding.myGalleryNameModifyBtn.visibility = View.GONE
            binding.myGalleryNameCheckBtn.visibility = View.VISIBLE
            binding.myGalleryNameTv.apply {
                isClickable = true
                isFocusable = true
            }
        }

        binding.myGalleryNameCheckBtn.setOnClickListener {
            binding.myGalleryNameModifyBtn.visibility = View.VISIBLE
            binding.myGalleryNameCheckBtn.visibility = View.GONE
            binding.myGalleryNameTv.apply {
                isClickable = false
                isFocusable = false
            }
        }
    }

}