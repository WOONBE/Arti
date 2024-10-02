package com.hexa.arti.ui.artwork

import android.util.Log
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.map
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.databinding.FragmentSelectArtworkBinding
import com.hexa.arti.ui.artwork.adapter.SelectArtworkAdapter
import com.hexa.arti.ui.search.paging.ArtworkPagingAdapter
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.min

private const val TAG = "SelectArtworkFragment"

@AndroidEntryPoint
class SelectArtworkFragment :
    BaseFragment<FragmentSelectArtworkBinding>(R.layout.fragment_select_artwork) {

    private val selectArtworkViewModel: SelectArtworkViewModel by viewModels()
    private lateinit var adapter: SelectArtworkAdapter
    private var isClicked = false
    private var tempList = listOf(Artwork(0, "", "","",""))
    private val artworkPagingAdapter = ArtworkPagingAdapter {
        Log.d("확인", "페이징 아이템 클릭")
    }
    override fun init() {

        adapter = SelectArtworkAdapter(onClick = { id ->
            val action =
                SelectArtworkFragmentDirections.actionSelectArtworkFragmentToIsSelectCreateImageFragment(
                    id
                )
            navigate(action)
        }
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                selectArtworkViewModel.artWorkResult.collectLatest { pagingData ->
                    Log.d("확인", "PagingData 수신 완료")

                    // PagingData를 List로 변환
                    val currentList = mutableListOf<Artwork>()
                    pagingData.map { artworkResponseItem ->
                        // 응답 데이터를 Artwork 객체로 변환
                        val artwork = Artwork(
                            artworkId = artworkResponseItem.artworkId,     // artwork_id를 artworkId로 매핑
                            title = artworkResponseItem.title,              // title 매핑
                            imageUrl = artworkResponseItem.imageUrl,        // filename을 imageUrl로 매핑
                            description = artworkResponseItem.description,  // description 매핑
                            year = artworkResponseItem.year                 // year 매핑
                        )

                        currentList.add(artwork)
                    }

                    // 리스트 크기 출력 (비어있는지 확인)
                    Log.d("확인", "수집된 아트워크 수: ${currentList.size}")

                    // tempList에 변환한 List 추가
                    tempList = currentList.toList()

                    // tempList 출력 (확인용)
                    tempList.forEach { artwork ->
                        Log.d("확인", "템프 리스트 아트워크: $artwork")
                    }

                    // 추가된 데이터를 어댑터에 제출
                    adapter.submitList(tempList)

                    // 추가 로그로 리스트 크기 확인
                    Log.d("확인", "어댑터에 전달된 리스트 크기: ${tempList.size}")

                    // 필요한 경우 추가 작업
                    CoroutineScope(Dispatchers.Main).launch{
                        mainActivity.hideLoadingDialog()
                    }
                    binding.noSearchTv.visibility = if (tempList.isEmpty()) VISIBLE else GONE
                }
            }
        }
//        selectArtworkViewModel.artWorkResult.observe(viewLifecycleOwner) {
//            isClicked = false
//            Log.d(TAG, "init: ${it}")
//            if (it.isNotEmpty()) {
//                binding.noSearchTv.visibility = GONE
//                Log.d(TAG, "init: bb")
//                mainActivity.hideLoadingDialog()
//                tempList = it.toList()
//                adapter.submitList(tempList)
//
//            }
//            else{
//                mainActivity.hideLoadingDialog()
//                binding.noSearchTv.visibility = VISIBLE
//            }
//        }

        with(binding) {

            selectArtworkRv.adapter = adapter
            if (!isClicked) {

                artworkSearchEt.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
                    ) {
                        // 검색 또는 엔터 키가 눌렸을 때
                        isClicked = true
                        selectArtworkViewModel.getArtworkByString(artworkSearchEt.text.toString())
                        mainActivity.showLoadingDialog()
                        return@OnEditorActionListener true
                    }
                    false
                })
            }

            artworkBackBtn.setOnClickListener { popBackStack() }
        }
    }

}