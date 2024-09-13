package com.hexa.arti.ui.artGalleryDetail

import android.content.Context
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtGalleryDetailBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.ui.artGalleryDetail.adapter.GalleryDetailViewPagerAdapter
import com.hexa.arti.ui.artGalleryDetail.adapter.GalleryThemeMenuAdapter
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "ArtGalleryDetailFragmen"
class ArtGalleryDetailFragment : BaseFragment<FragmentArtGalleryDetailBinding>(R.layout.fragment_art_gallery_detail) {

    private lateinit var adapter: GalleryDetailViewPagerAdapter
    private val images = listOf(
        R.drawable.gallery_example,
        R.drawable.gallery_sample1,
        R.drawable.gallery_sample2
    )
    private val themeNames = listOf("절망", "사랑", "내 얼굴","절망", "사랑", "내 얼굴")

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false

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
                val action = ArtGalleryDetailFragmentDirections.actionArtGalleryDetailFragmentToArtDetailFragment(
                    imgId
                )
                navigate(action)

            })
        binding.viewPager.adapter = adapter
        // 기본 아이템 설정
        binding.viewPager.setCurrentItem(0, false) // 첫 번째 실제 아이템으로 이동

        // 미디어 플레이어 설정 (음악 파일 경로 또는 리소스)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.gallery_bgm)

        initEvent()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 페이지가 완전히 선택된 후의 위치를 처리
                handlePageSelected(position)
            }

        })

    }

    private fun initEvent(){
        with(binding){
            galleryDetailLeftIb.setOnClickListener {
                if (binding.viewPager.currentItem > 1) {
                    binding.viewPager.currentItem -= 1
                }
            }

            galleryDetailRightIb.setOnClickListener {
                if (binding.viewPager.currentItem < images.size-1 ) {
                    binding.viewPager.currentItem += 1

                }
            }
            // 음악 시작
            galleryBgmPlayPtn.setOnClickListener {
                if (!isPlaying) {
                    binding.galleryBgmPlayPtn.visibility = View.GONE
                    binding.galleryBgmStopBtn.visibility = View.VISIBLE
                    isPlaying = true

                    // 코루틴을 사용하여 음악 재생
                    viewLifecycleOwner.lifecycleScope.launch {
                        playMusic()
                    }
                }
            }
            // 음악 종료
            galleryBgmStopBtn.setOnClickListener {
                stopMusic()
                binding.galleryBgmPlayPtn.visibility = View.VISIBLE
                binding.galleryBgmStopBtn.visibility = View.GONE
            }

            galleryMenuBtn.setOnClickListener {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
            galleryExitBtn.setOnClickListener {
                popBackStack()
            }
        }

    }
    // 코루틴을 사용한 음악 재생 함수
    private suspend fun playMusic() {
        mediaPlayer.start()

        // 음악이 끝날 때까지 대기
        while (mediaPlayer.isPlaying) {
            delay(1000) // 1초마다 음악 상태 체크
        }

        // 음악이 끝나면 버튼 상태 변경
        isPlaying = false
        binding.galleryBgmPlayPtn.visibility = View.VISIBLE
        binding.galleryBgmStopBtn.visibility = View.GONE
    }

    // 음악 정지 함수
    private fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0) // 음악을 처음으로 돌림
        }
        isPlaying = false
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
        mainActivity.changeLandScope()
        mainActivity.hideBottomNav(true)
    }
    // 뷰가 파괴될 때 MediaPlayer 해제
    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer.release()
    }
}