package com.abhishek.quotes.domain.model.quote

import com.squareup.moshi.JsonClass
import java.net.URL
import java.net.URLDecoder

@JsonClass(generateAdapter = true)
data class Quote(
    val id: Int = 0,
    val message: String,
    val author: String,
    val bgImagePath: String,
    val language: String,
    val publisherUsername: String,
    val bgImageUrl: String,
) {
    val formattedAuthor = "~ $author"
    fun decodedImageUrl() = URLDecoder.decode(bgImageUrl, "UTF-8")
}