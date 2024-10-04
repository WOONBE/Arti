package com.hexa.arti.ui.artwork.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hexa.arti.R
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.databinding.ItemSelectArtworkBinding

class SelectArtworkAdapter(val onClick: (Int) -> Unit) :
    PagingDataAdapter<Artwork, SelectArtworkAdapter.ArtworkViewHolder>(ArtworkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return ArtworkViewHolder(ItemSelectArtworkBinding.inflate(inflater, parent, false), onClick)
    }

    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class ArtworkViewHolder(
        private val binding: ItemSelectArtworkBinding,
        private val onClick: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = itemView.findViewById(R.id.select_art_iv)

        fun bind(artwork: Artwork) {
            with(imageView) {
                Glide.with(binding.root.context)
                    .load(artwork.imageUrl)
                    .error(R.drawable.gallery_sample1)
                    .into(binding.selectArtIv)
                setOnClickListener {
                    onClick(artwork.artworkId)
                }
            }

        }
    }

    class ArtworkDiffCallback : DiffUtil.ItemCallback<Artwork>() {
        override fun areItemsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
            return oldItem.artworkId == newItem.artworkId
        }

        override fun areContentsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
            return oldItem == newItem
        }
    }
}