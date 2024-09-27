package com.hexa.arti.ui.search.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hexa.arti.R
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.databinding.ItemArtBinding
import com.hexa.arti.ui.search.adapter.ArtDiffUtil

class ArtworkPagingAdapter(
    private val onItemClick: (Artwork) -> Unit
) : PagingDataAdapter<Artwork, ArtworkPagingAdapter.ArtViewHolder>(ArtDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArtViewHolder(
            ItemArtBinding.inflate(inflater, parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class ArtViewHolder(
        private val binding: ItemArtBinding,
        private val onItemClick: (Artwork) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artwork: Artwork) {
            Glide.with(binding.root.context)
                .load(artwork.imageUrl)
                .error(R.drawable.gallery_example)
                .into(binding.ivArt)

            binding.tvArtTitle.text = artwork.title

            itemView.setOnClickListener {
                onItemClick(artwork)
            }
        }
    }
}