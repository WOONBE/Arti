package com.hexa.arti.ui.home.adapter

import android.content.res.Resources.Theme
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hexa.arti.data.model.home.ArtTheme
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

        private val themeAdapter = ThemeAdapter()

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
                        when (newState) {
                            BottomSheetBehavior.STATE_DRAGGING -> {
                                onSliding()
                            }

                            BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED, BottomSheetBehavior.STATE_SETTLING -> {
                                onNormal()
                            }
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        if (lastOffset < slideOffset) {
                            if (slideOffset > 0.25f) {
                                stateFlag = 1
                            }
                        } else {
                            if (slideOffset < 0.806) {
                                stateFlag = 2
                            }
                        }
                        lastOffset = slideOffset
                    }
                })

            }


            binding.includeBottomSheet.ivPlay.setOnClickListener {
                onPlayClick(item)
            }

            binding.includeBottomSheet.rvTheme.apply{
                adapter = themeAdapter
                val artThemeList = listOf(
                    ArtTheme("절망"),
                    ArtTheme("희망"),
                    ArtTheme("병현")
                )

                themeAdapter.submitList(artThemeList)
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