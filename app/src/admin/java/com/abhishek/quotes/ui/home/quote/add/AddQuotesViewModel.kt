package com.abhishek.quotes.ui.home.quote.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.quotes.data.network.QuotesAPI
import com.abhishek.quotes.domain.model.network.ResponseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class AddQuotesViewModel @Inject constructor(
    private val quotesAPI: QuotesAPI
) : ViewModel() {

    private val _addQuoteStatus = MutableStateFlow<ResponseStatus>(ResponseStatus.UNDEFINED)
    val addQuoteStatus = _addQuoteStatus.asStateFlow()

    fun addQuote(message: String, author: String, lang: String, imageFile: File) {
        viewModelScope.launch {
            _addQuoteStatus.update { ResponseStatus.FETCHING }
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)
            val imageBody = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val response = quotesAPI.addQuote(message, author, lang, imageBody)
            _addQuoteStatus.update {
                if (response.isSuccessful) ResponseStatus.FETCHED
                else ResponseStatus.FAILURE(message = response.message())
            }
        }
    }
}