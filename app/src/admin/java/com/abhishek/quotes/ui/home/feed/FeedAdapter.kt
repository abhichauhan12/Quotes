package com.abhishek.quotes.ui.home.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.quotes.R
import com.abhishek.quotes.databinding.FeedListItemBinding
import com.abhishek.quotes.domain.model.Quote
import com.bumptech.glide.Glide

class FeedAdapter : ListAdapter<Quote, FeedAdapter.FeedItemViewHolder>(FeedItemDiffUtil) {

    object FeedItemDiffUtil : DiffUtil.ItemCallback<Quote>() {
        override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote) = oldItem == newItem
    }

    class FeedItemViewHolder(private val binding: FeedListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: Quote) {
            binding.quote = quote
            binding.executePendingBindings()
            Glide.with(binding.root).load(quote.decodedImageUrl()).placeholder(R.mipmap.ic_launcher)
                .into(binding.quoteThumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        val binding = FeedListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FeedItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        holder.bind(quote = getItem(position))
    }

}