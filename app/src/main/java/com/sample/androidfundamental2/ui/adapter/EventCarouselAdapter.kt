package com.sample.androidfundamental2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.androidfundamental2.R
import com.sample.androidfundamental2.data.model.Event
import com.sample.androidfundamental2.databinding.ItemEventCarouselBinding

class EventCarouselAdapter(
    private val onItemClick: (Event) -> Unit
) : ListAdapter<Event, EventCarouselAdapter.CarouselViewHolder>(DIFF_CALLBACK) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemEventCarouselBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarouselViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
    
    inner class CarouselViewHolder(
        private val binding: ItemEventCarouselBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(event: Event) {
            binding.apply {
                tvCarouselEventName.text = event.name
                
                Glide.with(itemView.context)
                    .load(event.imageUrl)
                    .placeholder(R.color.placeholder_color)
                    .error(R.color.placeholder_color)
                    .into(ivCarouselEventImage)
                
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
