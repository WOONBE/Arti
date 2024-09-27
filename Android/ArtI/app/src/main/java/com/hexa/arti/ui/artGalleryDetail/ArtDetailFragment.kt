package com.hexa.arti.ui.artGalleryDetail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtDetailBinding
import com.hexa.arti.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ArtDetailFragment"
@AndroidEntryPoint
class ArtDetailFragment : BaseFragment<FragmentArtDetailBinding>(R.layout.fragment_art_detail) {

    private val args: ArtDetailFragmentArgs by navArgs()
    private val artDetailViewModel : ArtDetailViewModel by viewModels()
    private var selectedThemeId:Int = 1
    override fun init() {
        artDetailViewModel.getMyGalleryTheme(1)
        artDetailViewModel.galleryTheme.observe(viewLifecycleOwner){
            Log.d(TAG, "init: $it")
            setupRadioButtons(it)
        }
        with(binding){
                Log.d(TAG, "init: ${args.imgUrl}")
                Glide.with(requireContext())
                    .load(args.imgUrl)
                    .into(artDetailIv)
                artDetailTitleTv.text = args.imgTitle
                artDetailAuthorTv.text = args.imgArtist
                artDetailCreateTv.text = args.imgYear

            artDetailCancelBtn.setOnClickListener {

                popBackStack()
            }
            artDetailBackBtn.setOnClickListener {
                artDetailCl.visibility = View.VISIBLE
                artDetailThemeCl.visibility = View.GONE
            }
            artDetailSubmitBtn.setOnClickListener {
                Log.d("Selected Theme ID", "Selected Theme ID: $selectedThemeId")
                artDetailCl.visibility = View.VISIBLE
                artDetailThemeCl.visibility = View.GONE
            }
            artDetailSaveBtn.setOnClickListener {
                artDetailCl.visibility = View.GONE
                artDetailThemeCl.visibility = View.VISIBLE
            }

        }

    }

    private fun setupRadioButtons(themes: List<Pair<Int, String>>) {
        val gridLayout = binding.gridLayout
        gridLayout.removeAllViews() // 기존 뷰 제거
        val radioGroup = RadioGroup(requireContext())
        radioGroup.orientation = RadioGroup.HORIZONTAL // 가로 배열

        themes.forEachIndexed { index, theme ->
            val radioButton = RadioButton(requireContext()).apply {
                id = theme.first  // theme id를 RadioButton id로 설정
                text = theme.second  // theme title을 RadioButton의 텍스트로 설정
                layoutParams = GridLayout.LayoutParams().apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT / 3  // GridLayout에서 비율로 배분
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(index % 3, 1f)  // 3열 배치
                    rowSpec = GridLayout.spec(index / 3)
                    setMargins(8, 8, 8, 8)  // 여백 설정
                }
            }

            // RadioGroup에 RadioButton 추가
            radioGroup.addView(radioButton)

            // RadioButton이 선택되었을 때의 동작
            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedThemeId = theme.first
                }
            }
        }

        // GridLayout에 RadioGroup 추가
        gridLayout.addView(radioGroup)
    }
}