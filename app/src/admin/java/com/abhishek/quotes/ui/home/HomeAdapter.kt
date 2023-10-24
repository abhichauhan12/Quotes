package com.abhishek.quotes.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.quotes.domain.model.quote.Quote

class HomeAdapter() : ListAdapter<ArrayList<Quote>, HomeAdapter.ViewHolder>(CheckItems) {

    object CheckItems : DiffUtil.ItemCallback<ArrayList<Quote>>(){
        override fun areItemsTheSame(oldItem: ArrayList<Quote>, newItem: ArrayList<Quote>): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArrayList<Quote>, newItem: ArrayList<Quote>): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val itemhomebinding: ItemHomeBinding) : RecyclerView.ViewHolder(itemhomebinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemhomebinding = ItemHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent ,false
        )
        return ViewHolder(itemhomebinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)

    }

}