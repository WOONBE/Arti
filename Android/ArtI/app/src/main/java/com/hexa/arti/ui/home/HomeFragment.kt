package com.hexa.arti.ui.home

import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentHomeBinding
import com.hexa.arti.ui.home.adapter.ViewpageAdapter
import com.hexa.arti.ui.MainActivity
import com.hexa.arti.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {


    private val viewModel: HomeViewModel by viewModels()

    override fun init() {
        initAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(false)
        mainActivity.changePortrait()
    }

    private fun initAdapter(){
        binding.viewpager2.adapter = ViewpageAdapter(
            onPlayClick = { itemNumber ->
                Log.d("확인", "클릭 확인요 ${itemNumber}")
                navigate(R.id.action_homeFragment_to_artGalleryDetailFragment)
            },
            onSliding = {
                binding.viewpager2.isUserInputEnabled = false
            },
            onNormal = {
                binding.viewpager2.isUserInputEnabled = true
            },
            blockTouch = {
//                val cancelEvent = MotionEvent.obtain(
//                    SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
//                    MotionEvent.ACTION_CANCEL, 0f, 0f, 0
//                )
//                view?.dispatchTouchEvent(cancelEvent)
//                cancelEvent.recycle()
            },
            unBlockTouch = {

            }
        )

    }


}