package com.hexa.arti.ui.search.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hexa.arti.R
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.databinding.ItemArtBinding

class ArtworkAdapter(
    private val onItemClick: (Artwork) -> Unit
) : ListAdapter<Artwork, ArtworkAdapter.ArtViewHolder>(ArtDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArtViewHolder(
            ItemArtBinding.inflate(inflater, parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ArtViewHolder(
        private val binding: ItemArtBinding,
        private val onItemClick: (Artwork) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artwork: Artwork) {
//            binding.tvArtTitle.text = art.title

            Glide.with(binding.root.context)
                .load(artwork.imageUrl)
                .error(R.drawable.gallery_example)
                .into(binding.ivArt)

            binding.tvArtTitle.text = "작품"

            itemView.setOnClickListener {
                onItemClick(artwork)
            }
        }
    }
}

class ArtDiffUtil : DiffUtil.ItemCallback<Artwork>() {
    override fun areItemsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
        return oldItem.artworkId == newItem.artworkId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
        return oldItem == newItem
    }
}