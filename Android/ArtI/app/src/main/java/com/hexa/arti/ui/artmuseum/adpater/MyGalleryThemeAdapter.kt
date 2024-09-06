package com.hexa.arti.ui.artmuseum.adpater

import android.animation.ValueAnimator
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
        private val themeTitleTv: TextView = view.findViewById(R.id.theme_title_tv)
        private val gridLayout: GridLayout = view.findViewById(R.id.my_gallery_theme_gridLayout)
        private val themeKebabMenuIv: ImageView = view.findViewById(R.id.theme_kebab_menu_iv)
        private var isGridVisible = false  // GridLayout이 보이는지 여부 추적

        // 각 아이템에 데이터 바인딩
        fun bind(item: MyGalleryThemeItem) {
            themeTitleTv.text = item.title
            // 이미지 리스트를 GridLayout에 동적으로 추가 (이미지 로드)
            gridLayout.removeAllViews() // 이전 이미지 제거
            item.images.forEach { imageResId ->
                val imageView = LayoutInflater.from(gridLayout.context).inflate(R.layout.gallery_theme_img, gridLayout, false) as ImageView
                imageView.setImageResource(imageResId)
                gridLayout.addView(imageView)
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
                        showEditDialog()
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

        private fun showEditDialog() {
            val context = themeTitleTv.context
            val builder = android.app.AlertDialog.Builder(context)

            // 다이얼로그 안에 EditText를 추가
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.dialog_edit_theme_title, null)
            val editText = dialogView.findViewById<EditText>(R.id.edit_theme_title)

            // 현재 themeTitleTv의 텍스트를 EditText에 설정
            editText.setText(themeTitleTv.text.toString())

            builder.setView(dialogView)
                .setTitle("테마 제목 수정")
                .setPositiveButton("수정") { dialog, id ->
                    // 사용자가 수정한 텍스트를 TextView에 반영
                    val newText = editText.text.toString()
                    themeTitleTv.text = newText
                }
                .setNegativeButton("취소") { dialog, id ->
                    dialog.cancel()
                }

            val dialog = builder.create()
            dialog.show()
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