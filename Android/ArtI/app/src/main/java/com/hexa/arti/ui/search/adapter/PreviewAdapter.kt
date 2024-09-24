package com.hexa.arti.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hexa.arti.R
import com.hexa.arti.data.model.search.PreviewImage
import com.hexa.arti.databinding.ItemPreviewImageBinding

class PreviewAdapter(
    private val onImageClick: (PreviewImage) -> Unit,
) : ListAdapter<PreviewImage, PreviewAdapter.PreviewViewHolder>(PreviewDiffUtil()) {


    class PreviewViewHolder(
        private val binding: ItemPreviewImageBinding,
        private val onImageClick: (PreviewImage) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(previewImage: PreviewImage) {
            if (previewImage.isFocus) {
                binding.clPreviewImage.setBackgroundResource(R.drawable.background_preview_focus)
            } else {
                binding.clPreviewImage.setBackgroundResource(0)
            }
            Glide.with(binding.root.context)
                .load(previewImage.url)
                .error(R.drawable.gallery_sample2)
                .into(binding.ivPreviewImage)
            itemView.setOnClickListener {
                onImageClick(previewImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PreviewViewHolder(
            ItemPreviewImageBinding.inflate(inflater, parent, false),
            onImageClick
        )
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PreviewDiffUtil : DiffUtil.ItemCallback<PreviewImage>() {
    override fun areItemsTheSame(oldItem: PreviewImage, newItem: PreviewImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PreviewImage, newItem: PreviewImage): Boolean {
        return oldItem == newItem
    }

}