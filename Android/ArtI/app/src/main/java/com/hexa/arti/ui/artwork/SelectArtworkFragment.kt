package com.hexa.arti.ui.artwork

import android.util.Log
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.databinding.FragmentSelectArtworkBinding
import com.hexa.arti.ui.artwork.adapter.SelectArtworkAdapter
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min

private const val TAG = "SelectArtworkFragment"

@AndroidEntryPoint
class SelectArtworkFragment :
    BaseFragment<FragmentSelectArtworkBinding>(R.layout.fragment_select_artwork) {

    private val selectArtworkViewModel: SelectArtworkViewModel by viewModels()
    private lateinit var adapter: SelectArtworkAdapter
    private var isClicked = false
    private var tempList = listOf(Artwork(0, "", ""))

    override fun init() {

        adapter = SelectArtworkAdapter(onClick = { id ->
            val action =
                SelectArtworkFragmentDirections.actionSelectArtworkFragmentToIsSelectCreateImageFragment(
                    id
                )
            navigate(action)
        }
        )

        selectArtworkViewModel.artworkResult.observe(viewLifecycleOwner) {
            isClicked = false
            Log.d(TAG, "init: ${it}")
            if (it.isNotEmpty()) {
                binding.noSearchTv.visibility = GONE
                Log.d(TAG, "init: bb")
                mainActivity.hideLoadingDialog()
                tempList = it.toList()
                adapter.submitList(tempList)

            }
            else{
                mainActivity.hideLoadingDialog()
                binding.noSearchTv.visibility = VISIBLE
            }
        }

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
                        selectArtworkViewModel.getSearchArtWork(artworkSearchEt.text.toString())
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