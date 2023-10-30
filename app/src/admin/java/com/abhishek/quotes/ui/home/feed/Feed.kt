package com.abhishek.quotes.ui.home.feed

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhishek.quotes.R
import com.abhishek.quotes.databinding.FragmentFeedBinding
import com.abhishek.quotes.domain.model.network.ResponseStatus
import com.abhishek.quotes.ui.utils.PaginationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Feed : Fragment(R.layout.fragment_feed) {

    private val feedViewModel by viewModels<FeedViewModel>()

    private lateinit var binding: FragmentFeedBinding
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var paginationManager: PaginationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        attachObservers()
        fetchQuotes()
    }

    private fun fetchQuotes() {
        if (feedViewModel.quotes.value.isEmpty())
            feedViewModel.fetchQuotes()
    }

    private fun initViews(view: View) {
        binding = FragmentFeedBinding.bind(view)
        binding.addNewQuoteFab.setOnClickListener { findNavController().navigate(FeedDirections.actionFeedScreenToAddQuotes()) }

        feedAdapter = FeedAdapter(
            onQuotePressed = {
                findNavController().navigate(FeedDirections.actionFeedScreenToEditQuote(it))
            }
        )

        paginationManager = PaginationManager {
            Toast.makeText(requireContext(), "Fetching more quotes...", Toast.LENGTH_SHORT).show()
            feedViewModel.fetchQuotes()
        }
        binding.quotesList.apply {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(paginationManager)
        }
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

            launch {
                feedViewModel.quoteFetchStatus.collectLatest {
                    when (it) {
                        ResponseStatus.UNDEFINED -> Unit
                        ResponseStatus.FETCHING -> Unit
                        is ResponseStatus.FAILURE -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            paginationManager.isLoading = false
                        }

                        ResponseStatus.FETCHED -> {
                            paginationManager.isLoading = false
                        }
                    }
                }
            }

        }
    }

}