package com.abhishek.quotes.ui.home.quote.edit

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
class EditQuotesViewModel @Inject constructor(
    private val quotesAPI: QuotesAPI
) : ViewModel() {

    private val _editQuoteStatus = MutableStateFlow<ResponseStatus>(ResponseStatus.UNDEFINED)
    val editQuoteStatus = _editQuoteStatus.asStateFlow()

    fun editQuote(id: Int, message: String, author: String, lang: String, imageFile: File?) {
        viewModelScope.launch {
            _editQuoteStatus.update { ResponseStatus.FETCHING }
            val requestFile = imageFile?.let { RequestBody.create(MediaType.parse("multipart/form-data"), imageFile) }
            val imageBody = requestFile?.let { MultipartBody.Part.createFormData("image", imageFile.name, requestFile) }

            val response = quotesAPI.updateQuote(id, message, author, lang, imageBody ?: MultipartBody.Part.createFormData("", ""))
            _editQuoteStatus.update {
                if (response.isSuccessful) ResponseStatus.FETCHED
                else ResponseStatus.FAILURE(message = response.message())
            }
        }
    }

}