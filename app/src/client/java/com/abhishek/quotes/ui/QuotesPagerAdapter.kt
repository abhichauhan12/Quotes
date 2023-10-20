package com.abhishek.quotes.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.abhishek.quotes.domain.model.quote.Quote

class QuotesPagerAdapter(
    activity: FragmentActivity,
) : FragmentStateAdapter(activity) {

    private val quotes: ArrayList<Quote> = ArrayList()

    override fun getItemCount(): Int = quotes.size

    override fun createFragment(position: Int): Fragment = QuoteScreen(quote = quotes[position])

    fun addQuotes(quotes: List<Quote>) {
        val diff = DiffUtil.calculateDiff(PagerDiffUtil(this.quotes, quotes))
        this.quotes.clear()
        this.quotes.addAll(quotes)
        diff.dispatchUpdatesTo(this)
    }
}

class PagerDiffUtil(private val oldList: List<Quote>, private val newList: List<Quote>) : DiffUtil.Callback() {

    enum class PayloadKey {
        VALUE
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return listOf(PayloadKey.VALUE)
    }
}