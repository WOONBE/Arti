package com.hexa.arti.ui.search.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.data.model.search.Art
import com.hexa.arti.databinding.ItemArtBinding

class ArtAdapter(
    private val onItemClick: (Art) -> Unit
) : ListAdapter<Art, ArtAdapter.ArtViewHolder>(ArtDiffUtil()) {


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
        private val onItemClick: (Art) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(art: Art) {
//            binding.tvArtTitle.text = art.title
            itemView.setOnClickListener {
                onItemClick(art)
            }
        }
    }
}

class ArtDiffUtil : DiffUtil.ItemCallback<Art>() {
    override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
        return oldItem == newItem
    }
}