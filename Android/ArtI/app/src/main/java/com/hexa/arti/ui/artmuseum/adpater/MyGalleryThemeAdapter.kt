package com.hexa.arti.ui.artmuseum.adpater

import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.R
import com.hexa.arti.data.MyGalleryThemeItem
import com.hexa.arti.ui.artmuseum.util.MyGalleryThemeDiffCallback

class MyGalleryThemeAdapter : ListAdapter<MyGalleryThemeItem, MyGalleryThemeAdapter.MyGalleryThemeViewHolder>(
    MyGalleryThemeDiffCallback
) {

    // ViewHolder 정의
    inner class MyGalleryThemeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val themeTitleTv: EditText = view.findViewById(R.id.theme_title_tv)
        private val gridLayout: GridLayout = view.findViewById(R.id.my_gallery_theme_gridLayout)
        private val themeKebabMenuIv: ImageView = view.findViewById(R.id.theme_kebab_menu_iv)
        private val themeModifyIv: ImageView = view.findViewById(R.id.theme_check_iv)
        private val themeCancelIv: ImageView = view.findViewById(R.id.theme_cancel_iv)
        private var initString = ""

        // 각 아이템에 데이터 바인딩
        fun bind(item: MyGalleryThemeItem) {
            themeTitleTv.setText(item.title)
            // 이미지 리스트를 GridLayout에 동적으로 추가 (이미지 로드)
            gridLayout.removeAllViews() // 이전 이미지 제거
            item.images.forEachIndexed { index, imageResId ->
                val imageView = LayoutInflater.from(gridLayout.context).inflate(R.layout.gallery_theme_img, gridLayout, false) as ImageView
                imageView.setImageResource(imageResId)
                gridLayout.addView(imageView)

                // 이미지에 long click listener 추가
                imageView.setOnLongClickListener {
                    showDeleteConfirmationDialog(imageView.context) {
                        removeImage(item, index) // 삭제 확인 후 이미지 삭제
                    }
                    true
                }
            }

            // 초기 GridLayout은 보이지 않도록 설정 (scaleY를 0으로)
            gridLayout.visibility = View.GONE

            // TextView 클릭 시 애니메이션 처리
            themeTitleTv.setOnClickListener {
                toggleGridLayout(gridLayout)
            }

            themeKebabMenuIv.setOnClickListener { view ->
                showPopupMenu(view)
            }
            themeModifyIv.setOnClickListener {
                themeModifyIv.visibility = View.GONE
                themeCancelIv.visibility = View.GONE
                themeTitleTv.apply {
                    isFocusable = false
                    isClickable = false
                }

            }
            themeCancelIv.setOnClickListener {
                themeModifyIv.visibility = View.GONE
                themeCancelIv.visibility = View.GONE
                themeTitleTv.apply {
                    setText(initString)
                    isFocusable = false
                    isClickable = false
                }
            }

        }
        fun toggleGridLayout(gridLayout: GridLayout) {
            val isExpanded = gridLayout.visibility == View.VISIBLE

            if (isExpanded) {
                // 축소 애니메이션
                val initialHeight = gridLayout.height
                val animator = ValueAnimator.ofInt(initialHeight, 0)
                animator.addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int
                    gridLayout.layoutParams.height = value
                    gridLayout.requestLayout()
                }
                animator.doOnEnd {
                    gridLayout.visibility = View.GONE // 축소 후 숨김 처리
                }
                animator.duration = 300 // 애니메이션 지속 시간 (ms)
                animator.start()
            } else {
                // 확장 애니메이션
                gridLayout.visibility = View.VISIBLE

                // 먼저 height를 0으로 설정하여 애니메이션 시작 전에 숨겨진 상태로 만듦
                gridLayout.layoutParams.height = 0
                gridLayout.measure(
                    View.MeasureSpec.makeMeasureSpec(gridLayout.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                val targetHeight = gridLayout.measuredHeight

                val animator = ValueAnimator.ofInt(0, targetHeight)
                animator.addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int
                    gridLayout.layoutParams.height = value
                    gridLayout.requestLayout()
                }

                animator.duration = 300 // 애니메이션 지속 시간 (ms)
                animator.start()
            }
        }
        private fun showPopupMenu(view: View) {
            val popupMenu = PopupMenu(view.context, view)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.gallery_modify_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.my_gallery_modify -> {
                        themeModifyIv.visibility = View.VISIBLE
                        themeCancelIv.visibility = View.VISIBLE
                        themeTitleTv.apply {
                            initString = this.text.toString()
                            isFocusableInTouchMode = true
                            isClickable = true
                            requestFocus() // 포커스를 EditText로 이동
                            setSelection(text.length) // 커서를 텍스트 끝으로 이동
                        }
                        true
                    }
                    R.id.my_gallery_delete -> {
                        // "삭제" 메뉴 클릭 처리
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }



        private fun showDeleteConfirmationDialog(context: Context, onConfirm: () -> Unit) {
            val builder = android.app.AlertDialog.Builder(context)
            builder.setTitle("이미지 삭제")
                .setMessage("이미지를 삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, _ ->
                    onConfirm() // 확인 버튼 클릭 시 실행할 동작 (이미지 삭제)
                    dialog.dismiss()
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss() // 취소 버튼 클릭 시 다이얼로그 닫기
                }
            val dialog = builder.create()
            dialog.show()
        }
        private fun removeImage(item: MyGalleryThemeItem, imageIndex: Int) {
            val updatedImages = item.images.toMutableList()
            updatedImages.removeAt(imageIndex)

            // 새로운 리스트로 갱신 (ListAdapter는 이 데이터를 기반으로 UI를 갱신함)
            submitList(currentList.map {
                if (it == item) it.copy(images = updatedImages) else it
            })
        }

    }

    // onCreateViewHolder: ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGalleryThemeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_gallery_theme, parent, false)
        return MyGalleryThemeViewHolder(view)
    }

    // onBindViewHolder: 데이터와 뷰 바인딩
    override fun onBindViewHolder(holder: MyGalleryThemeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}