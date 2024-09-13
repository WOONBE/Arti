package com.hexa.arti.ui.artmuseum.adpater

import androidx.recyclerview.widget.ListAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.data.model.artmuseum.Subscriber
import com.hexa.arti.databinding.ItemSubscribeBinding

class SubScribeAdapter : ListAdapter<Subscriber, SubScribeAdapter.SubscriberViewHolder>(SubscriberDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        val binding = ItemSubscribeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        val subscriber = getItem(position)
        holder.bind(subscriber)
    }

    inner class SubscriberViewHolder(private val binding: ItemSubscribeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subscriber: Subscriber) {
            binding.apply {
                subscribeProfileIv.setImageResource(subscriber.profileImageResId)
                subscribeNameTv.text = subscriber.name
                subscribeUsernameTv.text = subscriber.username
            }
        }
    }

    private class SubscriberDiffCallback : DiffUtil.ItemCallback<Subscriber>() {
        override fun areItemsTheSame(oldItem: Subscriber, newItem: Subscriber): Boolean {
            // Assuming Subscriber's identity is its name. Adjust according to your data model.
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Subscriber, newItem: Subscriber): Boolean {
            return oldItem == newItem
        }
    }
}
