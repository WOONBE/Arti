package com.hexa.arti.ui.artGalleryDetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.R

class GalleryDetailViewPagerAdapter(private val originalList: List<Int>,
                                    private val onImgClick: (Int) -> Unit,) : RecyclerView.Adapter<GalleryDetailViewPagerAdapter.GalleryViewHolder>() {

    init {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_detail, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(originalList[position])
    }

    override fun getItemCount(): Int = originalList.size

    inner class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(imageResId: Int) {
            itemView.findViewById<ImageView>(R.id.gallery_iv).setImageResource(imageResId)
            itemView.setOnClickListener {
                onImgClick(imageResId)
            }

        }
    }
}
