package com.hexa.arti.ui.artwork

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artworkupload.ArtWorkAIDto
import com.hexa.arti.databinding.FragmentArtworkResultBinding
import com.hexa.arti.ui.MainActivityViewModel
import com.hexa.arti.ui.MyGalleryActivityViewModel
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "확인"

@AndroidEntryPoint
class ArtworkResultFragment : BaseFragment<FragmentArtworkResultBinding>(R.layout.fragment_artwork_result) {

    private val args : ArtworkResultFragmentArgs by navArgs()
    private val artworkResultViewModel : ArtworkResultViewModel by viewModels()
    private val myGalleryActivityViewModel : MyGalleryActivityViewModel by activityViewModels()
    private val mainActivityViewModel : MainActivityViewModel by activityViewModels()
    data class Item(val id: Int, val name: String)
    private val itemList = arrayListOf(
        Item(1, "aa"),
        Item(2, "bb"),
        Item(3, "cc"),
        Item(4, "dd")
    )
    private var artworkType = "AI"
    private var galleryId : Int = 0
    private var themeId : Int = 0
    private var userId : Int = 0
    override fun init() {
        val fragmentManager = requireActivity().supportFragmentManager
        Log.d("BackStack", "Back stack entry count: ${fragmentManager.backStackEntryCount}")
        lifecycleScope.launch {
            mainActivityViewModel.getLoginData().collect { d ->
                Log.d(TAG, "onCreate: ${d?.galleryId}")
                d?.let {
                    userId = d.memberId
                    galleryId = d.galleryId
                }
            }
        }
        Log.d(TAG, "init: 결과창")
        with(binding){

            artworkResultViewModel.successStatus.observe(viewLifecycleOwner){
                mainActivity.hideLoadingDialog()
                when(it){
                    1->{
                        myGalleryActivityViewModel.getMyGalleryTheme(galleryId)
                        makeToast("작품이 등록되었습니다.")
                        val action = ArtworkResultFragmentDirections.actionArtworkResultFragmentToHomeFragment()
                        navigate(action)
                    }
                    else ->{

                    }
                }
            }


            // 기본 세팅
            if(args.artType == 1){
                Glide.with(requireContext())
                    .load(args.artId)
                    .into(artworkResultImg)
                artworkResultEt.setText("")
            }
            else if(args.artType == 0){
                artworkResultViewModel.getArtWork(args.artId.toInt())
            }

            artworkResultViewModel.artworkResult.observe(viewLifecycleOwner){
                artworkResultEt.setText(it.title)
                Glide.with(requireContext())
                    .load(it.imageUrl)
                    .into(artworkResultImg)
            }

            myGalleryActivityViewModel.myGalleryTheme.observe(viewLifecycleOwner){
                itemList.clear()

                // MyGalleryThemeItem 데이터를 Item으로 변환하여 리스트에 추가
                it.forEach { themeItem ->
                    val item = Item(
                        id = themeItem.id,          // MyGalleryThemeItem의 id 사용
                        name = themeItem.title     // MyGalleryThemeItem의 title 사용
                    )
                    itemList.add(item)
                    //spinner
                    artworkResultThemeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, itemList.map { it.name }).apply {
                        setDropDownViewResource(R.layout.item_spinner_theme)
                    }
                }
            }

            artworkResultThemeSpinner.dropDownWidth = ViewGroup.LayoutParams.MATCH_PARENT
            artworkResultThemeSpinner.dropDownWidth = ViewGroup.LayoutParams.WRAP_CONTENT
            artworkResultThemeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = itemList[position]
                    themeId = selectedItem.id
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // 선택되지 않았을 때 처리할 내용
                }
            }

            artworkBackBtn.setOnClickListener {
                popBackStack()
            }
            artworkResultBtn.setOnClickListener {
                Log.d(TAG, "init: 1번 ${args.artType}")
                if(args.artType == 1){
                    Log.d(TAG, "init: 2번 ${args.artType}")
                    artworkResultViewModel.getAIArtworkId(ArtWorkAIDto(
                        ai_artwork_title = artworkResultEt.text.toString(),
                        artwork_type = artworkType,
                        ai_img_url = args.artId,
                        member_id = userId
                        ),themeId)
                }
                else{
                    artworkResultViewModel.saveArtwork(
                        themeId = themeId,
                        artworkId = args.artId.toInt(),
                        description = artworkResultEt.text.toString()
                    )
                }
                mainActivity.showLoadingDialog()

            }
        }
    }

}