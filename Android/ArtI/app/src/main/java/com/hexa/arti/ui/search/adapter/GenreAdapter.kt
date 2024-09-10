package com.hexa.arti.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.data.model.search.Genre
import com.hexa.arti.databinding.ItemGenreBinding

class GenreAdapter(
    private val onItemClick: () -> Unit
) : ListAdapter<Genre, GenreAdapter.GenreViewHolder>(GenreDiffUtil()) {


    class GenreViewHolder(
        private val binding: ItemGenreBinding,
        private val onItemClick: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GenreViewHolder(
            ItemGenreBinding.inflate(inflater, parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class GenreDiffUtil : DiffUtil.ItemCallback<Genre>() {
    override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem == newItem
    }

}