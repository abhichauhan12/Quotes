package com.abhishek.quotes.domain.model.network

sealed class QuoteFetchStatus {
    object UNDEFINED : QuoteFetchStatus()
    object FETCHING : QuoteFetchStatus()
    object FETCHED : QuoteFetchStatus()
    class FAILURE(val message: String?) : QuoteFetchStatus()
}