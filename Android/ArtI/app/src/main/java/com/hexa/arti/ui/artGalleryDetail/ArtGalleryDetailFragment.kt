package com.hexa.arti.ui.artGalleryDetail

import android.content.Context
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artmuseum.MyGalleryThemeItem
import com.hexa.arti.databinding.FragmentArtGalleryDetailBinding
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.ui.artGalleryDetail.adapter.GalleryDetailViewPagerAdapter
import com.hexa.arti.ui.artGalleryDetail.adapter.GalleryThemeMenuAdapter
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "ArtGalleryDetailFragmen"

@AndroidEntryPoint
class ArtGalleryDetailFragment : BaseFragment<FragmentArtGalleryDetailBinding>(R.layout.fragment_art_gallery_detail) {

    private val args : ArtGalleryDetailFragmentArgs by navArgs()

    private val artGalleryViewModel : ArtGalleryDetailViewModel by viewModels()
    private lateinit var adapter: GalleryDetailViewPagerAdapter


    private var imageSize = 0

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    
    override fun init() {



        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isEnabled = false
            requireActivity().onBackPressed()
            mainActivity.changePortrait()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // galleryId를 이용해 테마 데이터 요청
        val galleryId = args.galleryId
        artGalleryViewModel.getGallery(galleryId)

        // 테마 데이터를 관찰하고 업데이트
        artGalleryViewModel.galleryDetail.observe(viewLifecycleOwner) { themes ->
            if (themes != null) {
                // 테마와 이미지를 처리하여 ViewPager와 메뉴를 설정
                setupViewPager(themes)
                setupMenu(themes)
                // 페이지 복원은 ViewPager가 설정된 후에 한 번만 수행
                val pageNum = artGalleryViewModel.getPageNum()
                Log.d(TAG, "Restoring page in onViewCreated: $pageNum")
                binding.viewPager.setCurrentItem(pageNum, false)
            }
        }

        Log.d(TAG, "onViewCreated: ${artGalleryViewModel.getPageNum()}")


        // 미디어 플레이어 설정 (음악 파일 경로 또는 리소스)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.gallery_bgm)

        initEvent()

    }

    private fun initEvent(){
        with(binding){
            galleryDetailLeftIb.setOnClickListener {
                if (binding.viewPager.currentItem > 1) {
                    binding.viewPager.currentItem -= 1
                }
            }

            galleryDetailRightIb.setOnClickListener {
                if (binding.viewPager.currentItem < imageSize-1 ) {
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
                mainActivity.changePortrait()
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
                binding.galleryDetailRightIb.visibility = View.VISIBLE
            }
            imageSize-1 -> {
                binding.galleryDetailRightIb.visibility = View.GONE
                binding.galleryDetailLeftIb.visibility = View.VISIBLE
            }
            else->{
                binding.galleryDetailRightIb.visibility = View.VISIBLE
                binding.galleryDetailLeftIb.visibility = View.VISIBLE
            }
        }
    }

    private fun setupViewPager(themes: List<MyGalleryThemeItem>) {
        val combinedImageList = themes.flatMap { it.images } // 모든 테마의 이미지를 합침
        // 각 테마의 첫 번째 이미지 인덱스를 저장해둠
        val themeStartIndices = mutableListOf<Int>()
        var currentIndex = 0
        themes.forEach { theme ->
            themeStartIndices.add(currentIndex)
            currentIndex += theme.images.size
        }
        imageSize = currentIndex
        val adapter = GalleryDetailViewPagerAdapter(combinedImageList) { dto ->
            val action = ArtGalleryDetailFragmentDirections.actionArtGalleryDetailFragmentToArtDetailFragment(imgId = dto.id, imgTitle = dto.title,imgUrl = dto.imageUrl, imgYear = dto.year, imgArtist = dto.artist, galleryId = args.galleryId)
            navigate(action)
        }
        binding.viewPager.adapter = adapter
        // ViewPager 어댑터가 설정된 후에 페이지 복원

        // ViewPager 페이지 변경 시 테마 제목 업데이트
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d(TAG, "onPageSelected: ${position}")
                artGalleryViewModel.updatePageNum(position)
                // 현재 선택된 페이지가 속한 테마를 찾아 테마 제목을 갱신
                val currentThemeIndex = themeStartIndices.indexOfLast { it <= position }
                binding.galleryThmemTv.text = "테마 : ${themes[currentThemeIndex].title}"
                handlePageSelected(position)
            }
        })
    }

    private fun setupMenu(themes: List<MyGalleryThemeItem>) {
        val themeTitles = themes.map { it.title }
        binding.galleryThemeRv.adapter = GalleryThemeMenuAdapter(themeTitles) { position ->

            if (themes[position].images.isEmpty()) {
                // 이미지가 없으면 테마 변경을 막음
                makeToast("선택된 테마는 작품이 없습니다.")
                return@GalleryThemeMenuAdapter
            }

            val firstImageIndex = themes.subList(0, position).sumOf { it.images.size } // 선택된 테마의 첫 번째 이미지 인덱스 계산

            binding.viewPager.setCurrentItem(firstImageIndex, false)
            binding.galleryThmemTv.text = "테마 : ${themeTitles[position]}"
            binding.drawerLayout.closeDrawers()
        }
    }


    override fun onResume() {
        super.onResume()
        mainActivity.changeLandScope()
    }
    // 뷰가 파괴될 때 MediaPlayer 해제
    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer.release()
    }
}