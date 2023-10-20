package com.abhishek.quotes.domain.model.network

sealed class ResponseStatus {
    object UNDEFINED : ResponseStatus()
    object FETCHING : ResponseStatus()
    object FETCHED : ResponseStatus()
    class FAILURE(val message: String?) : ResponseStatus()
}