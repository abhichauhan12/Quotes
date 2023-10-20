package com.abhishek.quotes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.quotes.data.network.QuoteFetchAPI
import com.abhishek.quotes.domain.model.network.QuoteFetchStatus
import com.abhishek.quotes.domain.model.quote.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesViewModel @Inject constructor(
    private val quoteFetchAPI: QuoteFetchAPI
) : ViewModel() {

    private val _quotesFetchStatus = MutableStateFlow<QuoteFetchStatus>(value = QuoteFetchStatus.UNDEFINED)
    val quotesFetchStatus = _quotesFetchStatus.asStateFlow()

    private val _quotes = MutableStateFlow<List<Quote>>(value = ArrayList())
    val quotes = _quotes.asStateFlow()

    fun fetchQuotes(passViewedIds : Boolean = false) {
        viewModelScope.launch {
            if (quotes.value.isEmpty()) _quotesFetchStatus.update { QuoteFetchStatus.FETCHING }

            try {
                val viewedIds: String = if (passViewedIds)
                    quotes.value.joinToString(",") { it.id.toString() }
                else ""
                val response = quoteFetchAPI.getQuotes(viewedQuoteIds = viewedIds)
                if (response.isSuccessful) {
                    val oldQuotes = _quotes.value
                    val newQuotes = response.body() ?: ArrayList()
                    val updatedList = ArrayList<Quote>().apply {
                        addAll(oldQuotes)
                        addAll(newQuotes)
                    }

                    _quotes.update { updatedList }
                }

                _quotesFetchStatus.update {
                    if (response.isSuccessful) QuoteFetchStatus.FETCHED
                    else QuoteFetchStatus.FAILURE(message = "Network request failed")
                }
            }catch (e: Exception) {
                _quotesFetchStatus.update { QuoteFetchStatus.FAILURE(message = e.message) }
            }
        }
    }

}

