package com.hexa.arti.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hexa.arti.databinding.ItemHomepageBinding

class ViewpageAdapter(
    private val onPlayClick: (Int) -> Unit,
    private val onSliding: () -> Unit,
    private val onNormal: () -> Unit,
    private val blockTouch: () -> Unit,
    private val unBlockTouch: () -> Unit,
) : RecyclerView.Adapter<ViewpageAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ItemHomepageBinding,
        private val onPlayClick: (Int) -> Unit,
        private val onSliding: () -> Unit,
        private val onNormal: () -> Unit,
        private val blockTouch: () -> Unit,
        private val unBlockTouch: () -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Int) {
            val bottomSheet = binding.includeBottomSheet.bottomSheetLayout
            val bottomSheetBehavior: BottomSheetBehavior<View> =
                BottomSheetBehavior.from(bottomSheet)

            bottomSheetBehavior.apply {
                isHideable = false
                isFitToContents = false
                isDraggable = true
                var lastOffset = 0f
                var stateFlag = 0

                addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        println("현재 상태는 ${newState}")
                        when (newState) {
                            BottomSheetBehavior.STATE_DRAGGING -> {
                                onSliding()
//                                if (stateFlag == 1) {
//                                    state = BottomSheetBehavior.STATE_EXPANDED
//                                }
//                                else if (stateFlag == 2) {
//                                    state = BottomSheetBehavior.STATE_COLLAPSED
//                                }
//                                stateFlag = 0
                            }

                            BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED, BottomSheetBehavior.STATE_SETTLING -> {
//                                if (stateFlag == 1) {
//                                    println("현재 상태 플래그 1")
//                                    state = BottomSheetBehavior.STATE_EXPANDED
//                                }
//                                else if (stateFlag == 2) {
//                                    println("현재 상태 플래그 2")
//                                    state = BottomSheetBehavior.STATE_COLLAPSED
//                                }
//                                stateFlag = 0

                                onNormal()
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        if (lastOffset < slideOffset) {
                            if (slideOffset > 0.25f) {
                                stateFlag = 1
                                blockTouch()
                            }
                        } else {
                            if (slideOffset < 0.806) {
                                stateFlag = 2
                                unBlockTouch()
                            }
                        }
                        lastOffset = slideOffset
                    }
                })

            }


            binding.includeBottomSheet.ivPlay.setOnClickListener {
                onPlayClick(item)
            }

        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHomepageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onPlayClick, onSliding, onNormal, blockTouch, unBlockTouch)
    }

    override fun getItemCount(): Int {
        return 6
    }

}