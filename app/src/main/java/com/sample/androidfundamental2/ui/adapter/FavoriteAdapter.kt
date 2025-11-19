package com.sample.androidfundamental2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.androidfundamental2.R
import com.sample.androidfundamental2.data.local.entity.FavoriteEvent
import com.sample.androidfundamental2.databinding.ItemEventBinding

class FavoriteAdapter(
    private val onItemClick: (FavoriteEvent) -> Unit
) : ListAdapter<FavoriteEvent, FavoriteAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: FavoriteEvent) {
            with(binding) {
                tvEventName.text = event.name
                tvEventOwner.text = event.ownerName

                Glide.with(root.context)
                    .load(event.imageUrl)
                    .placeholder(R.drawable.outline_badminton_24)
                    .into(ivEventImage)

                root.setOnClickListener {
                    onItemClick(event)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEvent>() {
            override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}
