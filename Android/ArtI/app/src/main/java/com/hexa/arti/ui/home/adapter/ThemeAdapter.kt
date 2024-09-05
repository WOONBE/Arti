package com.hexa.arti.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.R
import com.hexa.arti.data.model.home.ArtTheme
import com.hexa.arti.databinding.ItemThemeBinding

class ThemeAdapter() : ListAdapter<ArtTheme, ThemeAdapter.ThemeViewHolder>(ThemeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ThemeViewHolder(ItemThemeBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ThemeViewHolder(
        private val binding: ItemThemeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artTheme: ArtTheme) {
            binding.tvThemeTitle.text = binding.root.context.getString(
                R.string.theme_title,
                artTheme.title
            )
        }
    }
}

class ThemeDiffUtil : DiffUtil.ItemCallback<ArtTheme>() {
    override fun areItemsTheSame(oldItem: ArtTheme, newItem: ArtTheme): Boolean {
        return oldItem.title == newItem.title
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ArtTheme, newItem: ArtTheme): Boolean {
        return oldItem == newItem
    }
}