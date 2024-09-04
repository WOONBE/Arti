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
) : RecyclerView.Adapter<ViewpageAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ItemHomepageBinding,
        private val onPlayClick: (Int) -> Unit,
        private val onSliding: () -> Unit,
        private val onNormal: () -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Int) {
            val bottomSheet = binding.includeBottomSheet.bottomSheetLayout
            val bottomSheetBehavior: BottomSheetBehavior<View> =
                BottomSheetBehavior.from(bottomSheet)

            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.halfExpandedRatio = 0.5f
            bottomSheetBehavior.isFitToContents = false

            var bottomSheetFlag = false

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            onSliding()
                        }

                        BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED-> {
                            onNormal()
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }


            })

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
        return ViewHolder(binding, onPlayClick, onSliding, onNormal)
    }

    override fun getItemCount(): Int {
        return 6
    }

}