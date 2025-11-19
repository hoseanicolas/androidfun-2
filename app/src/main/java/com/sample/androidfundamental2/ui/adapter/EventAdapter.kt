package com.sample.androidfundamental2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.androidfundamental2.R
import com.sample.androidfundamental2.data.model.Event
import com.sample.androidfundamental2.databinding.ItemEventBinding

class EventAdapter(
    private val onItemClick: (Event) -> Unit
) : ListAdapter<Event, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
    
    inner class EventViewHolder(
        private val binding: ItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(event: Event) {
            binding.apply {
                tvEventName.text = event.name
                tvEventOwner.text = event.ownerName
                
                Glide.with(itemView.context)
                    .load(event.imageUrl)
                    .placeholder(R.color.placeholder_color)
                    .error(R.color.placeholder_color)
                    .into(ivEventImage)
                
                root.setOnClickListener {
                    onItemClick(event)
                }
            }
        }
    }
    
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.id == newItem.id
            }
            
            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        }
    }
}
