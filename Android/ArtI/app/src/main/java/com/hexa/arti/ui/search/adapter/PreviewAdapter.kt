package com.hexa.arti.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.data.model.search.PreviewImage
import com.hexa.arti.databinding.ItemPreviewImageBinding

class PreviewAdapter(
    private val onImageClick: () -> Unit,
) : ListAdapter<PreviewImage, PreviewAdapter.PreviewViewHolder>(PreviewDiffUtil()) {


    class PreviewViewHolder(
        private val binding: ItemPreviewImageBinding,
        private val onImageClick: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(previewImage: PreviewImage) {
            itemView.setOnClickListener {
                onImageClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PreviewViewHolder(
            ItemPreviewImageBinding.inflate(inflater,parent,false),
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