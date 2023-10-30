package com.abhishek.quotes.ui.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationManager(
    private val fetchMore : () -> Unit
) : RecyclerView.OnScrollListener()  {

    var isLoading = false

    private val visibleThreshold = 5
    private var lastVisibleItem = 0
    private var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager : LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

        totalItemCount = layoutManager.itemCount
        lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            fetchMore()
            isLoading = true
        }
    }
}