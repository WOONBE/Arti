package com.hexa.arti.ui.artmuseum

import android.content.Context
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentMyGalleryHomeBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.ui.artmuseum.adpater.MyGalleryAdapter

class MyGalleryHomeFragment : BaseFragment<FragmentMyGalleryHomeBinding>(R.layout.fragment_my_gallery_home){

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(false)
    }

    private val args : MyGalleryHomeFragmentArgs by navArgs()

    override fun init() {
        with(binding){
            myGalleryViewPager.adapter = MyGalleryAdapter(requireActivity())
            // ViewPager의 어댑터가 완전히 설정된 후에 currentItem을 설정하도록 post 메서드 사용
            myGalleryViewPager.post {
                when(args.myGalleryType) {
                    1 -> {
                        myGalleryViewPager.currentItem = 1
                    }
                    else -> {
                        myGalleryViewPager.currentItem = 0
                    }
                }
            }

            myGalleryViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    // 현재 선택된 페이지에 따라 스타일을 변경
                    when (position) {
                        0 -> {
                            subscribeTv.setTextAppearance(requireContext(), R.style.SelectTextTitle)
                            myGalleryTv.setTextAppearance(requireContext(), R.style.UnSelectTextTitle)
                            subscribeV.setBackgroundColor(resources.getColor(R.color.black))
                            myGalleryV.setBackgroundColor(resources.getColor(R.color.disable_color))
                        }
                        1 -> {
                            subscribeTv.setTextAppearance(requireContext(), R.style.UnSelectTextTitle)
                            myGalleryTv.setTextAppearance(requireContext(), R.style.SelectTextTitle)
                            subscribeV.setBackgroundColor(resources.getColor(R.color.disable_color))
                            myGalleryV.setBackgroundColor(resources.getColor(R.color.black))
                        }
                    }
                }
            })

            subscribeBtn.setOnClickListener {
                myGalleryViewPager.currentItem = 0
            }

            myGalleryBtn.setOnClickListener {
                myGalleryViewPager.currentItem = 1
            }

        }
    }

}