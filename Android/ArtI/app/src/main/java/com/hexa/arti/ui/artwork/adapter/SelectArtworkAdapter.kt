package com.hexa.arti.ui.artwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.R
import com.hexa.arti.data.model.artwork.Artwork

class SelectArtworkAdapter( val onClick: (Int) -> Unit,) : ListAdapter<Artwork, SelectArtworkAdapter.ArtworkViewHolder>(ArtworkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_select_artwork, parent, false) // item_artwork.xml은 아이템의 레이아웃 파일
        return ArtworkViewHolder(view,onClick)
    }

    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        val artwork = getItem(position)
        holder.bind(artwork)
    }

    class ArtworkViewHolder(itemView: View, val onClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.select_art_iv)

        fun bind(artwork: Artwork) {
            with(imageView){
                setImageResource(artwork.imageResId)
                setOnClickListener {
                    onClick(artwork.id)
                }
            }

        }
    }

    class ArtworkDiffCallback : DiffUtil.ItemCallback<Artwork>() {
        override fun areItemsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
            return oldItem.id == newItem.id // 고유한 아이디로 아이템 비교
        }

        override fun areContentsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
            return oldItem == newItem // 객체 전체를 비교해 변경 여부 판단
        }
    }
}