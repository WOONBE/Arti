package com.hexa.arti.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hexa.arti.R
import com.hexa.arti.data.model.search.Artist
import com.hexa.arti.databinding.ItemArtistBinding

class ArtistAdapter(
    private val onItemClick: (Artist) -> Unit
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
        private val onItemClick: (Artist) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist) {
            Glide.with(binding.ivArtist.context)
                .load(if (artist.imageUrl.isBlank()) R.drawable.basic_artist_profile else artist.imageUrl)
                .error(R.drawable.basic_artist_profile)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivArtist)

            binding.tvArtistName.text = artist.engName
            binding.tvArtistDescription.text = artist.description

            itemView.setOnClickListener {
                onItemClick(artist)
            }
        }
    }
}

class ArtistDiffUtil : DiffUtil.ItemCallback<Artist>() {
    override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem.artistId == newItem.artistId
    }

    override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem == newItem
    }

}