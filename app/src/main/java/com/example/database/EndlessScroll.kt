package com.example.database

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager


abstract class EndlessScroll(linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private var mLinearLayoutManager: LinearLayoutManager = linearLayoutManager
    private var visibleThreshold = 5
    private var currentPage = 1
    private var previousTotalItemCount = 0
    private var loading = true
    private var startingPageIndex = 0
    private var totalItemCount: Int = 0
    private var lastVisibleItemPosition: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        totalItemCount = mLinearLayoutManager.itemCount
        lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition()

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, recyclerView)
            loading = true
        }
    }

    abstract fun onLoadMore(pageNum: Int, recyclerView: RecyclerView)
}