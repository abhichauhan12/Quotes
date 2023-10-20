package com.abhishek.quotes.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.abhishek.quotes.R
import com.abhishek.quotes.databinding.FragmentHomeBinding
import com.abhishek.quotes.domain.model.network.QuoteFetchStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Home : Fragment(R.layout.fragment_home) {

    private val quotesViewModel by viewModels<QuotesViewModel>()
    private lateinit var binding: FragmentHomeBinding

    private lateinit var adapter: QuotesPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        binding.lifecycleOwner = this

        initViews()
        attachObservers()
        fetchQuotes()
    }

    private fun initViews() {
        adapter = QuotesPagerAdapter(activity = requireActivity())
        binding.quotesPager.adapter = adapter

        binding.quotesPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (quotesViewModel.quotes.value.isNotEmpty() && position == quotesViewModel.quotes.value.size - 5) {
                    quotesViewModel.fetchQuotes(passViewedIds = true)
                    Toast.makeText(requireContext(), "Fetching more quotes", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun attachObservers() {
        lifecycleScope.launch {
            launch {
                quotesViewModel.quotesFetchStatus.collectLatest { status ->
                    when(status) {
                        QuoteFetchStatus.UNDEFINED -> Unit

                        QuoteFetchStatus.FETCHING -> {
                            binding.fetchStatusLabel.apply {
                                visibility = View.VISIBLE
                                text = "Fetching quotes..."
                            }

                            binding.quotesPager.apply {
                                visibility = View.GONE
                            }
                        }

                        QuoteFetchStatus.FETCHED -> {
                            binding.fetchStatusLabel.apply {
                                visibility = View.GONE
                            }

                            binding.quotesPager.apply {
                                visibility = View.VISIBLE
                            }
                        }

                        is QuoteFetchStatus.FAILURE -> {
                            binding.fetchStatusLabel.apply {
                                visibility = View.VISIBLE
                                text = status.message
                            }

                            binding.quotesPager.apply {
                                visibility = View.GONE
                            }
                        }
                    }
                }
            }

            launch {
                quotesViewModel.quotes.collectLatest {
                    // add data in list adapter
                    if (it.isNotEmpty()) {
                        adapter.addQuotes(quotes = it)
                    }
                }
            }
        }
    }

    private fun fetchQuotes() {
        quotesViewModel.fetchQuotes()
    }
}

