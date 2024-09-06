package com.hexa.arti.ui.artGalleryDetail

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtGalleryDetailBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.ui.artGalleryDetail.adapter.GalleryDetailViewPagerAdapter
import com.hexa.arti.ui.artGalleryDetail.adapter.GalleryThemeMenuAdapter
import com.hexa.arti.util.popBackStack

private const val TAG = "ArtGalleryDetailFragmen"
class ArtGalleryDetailFragment : BaseFragment<FragmentArtGalleryDetailBinding>(R.layout.fragment_art_gallery_detail) {

    private lateinit var mainActivity : MainActivity
    private lateinit var adapter: GalleryDetailViewPagerAdapter
    private val images = listOf(
        R.drawable.gallery_example,
        R.drawable.gallery_sample1,
        R.drawable.gallery_sample2
    )
    private val themeNames = listOf("절망", "사랑", "내 얼굴","절망", "사랑", "내 얼굴")


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun init() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.galleryThemeRv.adapter = GalleryThemeMenuAdapter(themeNames,
            onMenuClick = { position ->
                Log.d(TAG, "onViewCreated: $position")
                binding.galleryThmemTv.text = "테마 : ${themeNames[position]}"
                binding.viewPager.setCurrentItem(position%3, false)
                binding.drawerLayout.closeDrawers()
            })

        adapter = GalleryDetailViewPagerAdapter(images,
            onImgClick = { imgId ->


            })
        binding.viewPager.adapter = adapter
        // 기본 아이템 설정
        binding.viewPager.setCurrentItem(0, false) // 첫 번째 실제 아이템으로 이동

        binding.galleryDetailLeftIb.setOnClickListener {
            if (binding.viewPager.currentItem > 1) {
                binding.viewPager.currentItem -= 1
            }
        }

        binding.galleryDetailRightIb.setOnClickListener {
            if (binding.viewPager.currentItem < images.size-1 ) {
                binding.viewPager.currentItem += 1

            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 페이지가 완전히 선택된 후의 위치를 처리
                handlePageSelected(position)
            }

        })

        binding.galleryBgmPlayPtn.setOnClickListener {
            binding.galleryBgmPlayPtn.visibility = View.GONE
            binding.galleryBgmStopBtn.visibility = View.VISIBLE
        }
        binding.galleryBgmStopBtn.setOnClickListener {
            binding.galleryBgmPlayPtn.visibility = View.VISIBLE
            binding.galleryBgmStopBtn.visibility = View.GONE
        }

        binding.galleryMenuBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
        binding.galleryExitBtn.setOnClickListener {
            popBackStack()
        }
    }



    private fun handlePageSelected(position: Int) {
        when(position){
            0 -> {
                binding.galleryDetailLeftIb.visibility = View.GONE
            }
            images.size-1 -> {
                binding.galleryDetailRightIb.visibility = View.GONE
            }
            else->{
                binding.galleryDetailRightIb.visibility = View.VISIBLE
                binding.galleryDetailLeftIb.visibility = View.VISIBLE
            }
        }
    }


    override fun onResume() {
        super.onResume()
        // 가로모드로 고정
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        mainActivity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  // 내비게이션 바 숨기기
                        or View.SYSTEM_UI_FLAG_FULLSCREEN       // 상태바 숨기기
                )
        mainActivity.hideBottomNav(true)
    }

    override fun onPause() {
        super.onPause()
        // 기본 모드로 복원
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        mainActivity.window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
    }
}