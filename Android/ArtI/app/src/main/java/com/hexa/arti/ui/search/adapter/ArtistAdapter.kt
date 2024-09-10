package com.hexa.arti.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.data.model.search.Artist
import com.hexa.arti.databinding.ItemArtistBinding

class ArtistAdapter(
    private val onItemClick: () -> Unit
) : ListAdapter<Artist, ArtistAdapter.ArtistViewHolder>(ArtistDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArtistViewHolder(
            ItemArtistBinding.inflate(inflater, parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ArtistViewHolder(
        private val binding: ItemArtistBinding,
        private val onItemClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist) {
//            binding.tvArtistName.text = "하하"
            itemView.setOnClickListener {
                onItemClick()
            }
        }
    }
}

class ArtistDiffUtil : DiffUtil.ItemCallback<Artist>() {
    override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem == newItem
    }

}