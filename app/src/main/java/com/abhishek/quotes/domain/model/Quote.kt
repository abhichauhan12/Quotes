package com.abhishek.quotes.domain.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.net.URL
import java.net.URLDecoder

@Parcelize
@JsonClass(generateAdapter = true)
data class Quote(
    val id: Int = 0,
    val message: String,
    val author: String,
    val bgImagePath: String,
    val language: String,
    val publisherUsername: String,
    val bgImageUrl: String,
) : Parcelable {
    val formattedAuthor = "~ $author"
    fun decodedImageUrl() = URLDecoder.decode(bgImageUrl, "UTF-8")
}