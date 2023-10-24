package com.abhishek.quotes.ui.home.feed

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.abhishek.quotes.R
import com.abhishek.quotes.databinding.FragmentFeedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Feed : Fragment(R.layout.fragment_feed) {

    private val feedViewModel by viewModels<FeedViewModel>()

    private lateinit var binding : FragmentFeedBinding
    private lateinit var feedAdapter: FeedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        attachObservers()
        fetchQuotes()
    }

    private fun fetchQuotes() {
        feedViewModel.fetchQuotes()
    }

    private fun initViews(view: View) {
        binding = FragmentFeedBinding.bind(view)
        binding.lifecycleOwner = this

        feedAdapter = FeedAdapter()
        binding.quotesList.adapter = feedAdapter
    }

    private fun attachObservers() {
        lifecycleScope.launch {
            launch {
                feedViewModel.adminData.collectLatest {
                    if (!it.username.isNullOrBlank())
                        binding.adminUsername.text = it.username
                }
            }

            launch {
                feedViewModel.quotes.collectLatest {
                    feedAdapter.submitList(it)
                }
            }

        }
    }

}