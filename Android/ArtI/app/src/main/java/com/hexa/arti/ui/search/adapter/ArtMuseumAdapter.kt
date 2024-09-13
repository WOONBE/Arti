package com.hexa.arti.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.data.model.search.ArtMuseum
import com.hexa.arti.databinding.ItemArtMuseumBinding

class ArtMuseumAdapter(
    private val onItemClick: () -> Unit
) : ListAdapter<ArtMuseum, ArtMuseumAdapter.ArtMuseumViewHolder>(ArtMuseumDiffUtil()) {

    class ArtMuseumViewHolder(
        private val binding: ItemArtMuseumBinding,
        private val onItemClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artMuseum: ArtMuseum) {
            itemView.setOnClickListener {
                onItemClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtMuseumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ArtMuseumViewHolder(
            ItemArtMuseumBinding.inflate(inflater, parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ArtMuseumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class ArtMuseumDiffUtil : DiffUtil.ItemCallback<ArtMuseum>() {
    override fun areItemsTheSame(oldItem: ArtMuseum, newItem: ArtMuseum): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArtMuseum, newItem: ArtMuseum): Boolean {
        return oldItem == newItem
    }
}
