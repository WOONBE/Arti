package com.hexa.arti.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hexa.arti.databinding.ItemHomepageBinding

class ViewpageAdapter() : RecyclerView.Adapter<ViewpageAdapter.ViewHolder>() {

    class ViewHolder(private val binding : ItemHomepageBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : Int){

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomepageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 6
    }

}