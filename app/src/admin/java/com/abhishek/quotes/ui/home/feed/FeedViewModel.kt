package com.abhishek.quotes.ui.home.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.quotes.Admin
import com.abhishek.quotes.data.network.QuotesAPI
import com.abhishek.quotes.data.proto.adminDataStore
import com.abhishek.quotes.domain.model.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    application: Application,
    private val quotesAPI: QuotesAPI
): AndroidViewModel(application) {

    val adminData : Flow<Admin> = application.adminDataStore.data

    private val _quotes = MutableStateFlow<List<Quote>>(value = ArrayList())
    val quotes = _quotes.asStateFlow()

    fun fetchQuotes() {
        viewModelScope.launch {
            try {
                val response = quotesAPI.getQuotes()
                if (response.isSuccessful) {
                    val oldQuotes = _quotes.value
                    val newQuotes = response.body() ?: ArrayList()
                    val updatedList = ArrayList<Quote>().apply {
                        addAll(oldQuotes)
                        addAll(newQuotes)
                    }
                    _quotes.update { updatedList }
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}