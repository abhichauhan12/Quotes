package com.abhishek.quotes.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.abhishek.quotes.R
import com.abhishek.quotes.databinding.FragmentQuoteScreenBinding
import com.abhishek.quotes.domain.model.Quote
import com.bumptech.glide.Glide

class QuoteScreen(
    private val quote: Quote,
) : Fragment(R.layout.fragment_quote_screen) {

    private lateinit var binding: FragmentQuoteScreenBinding

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentQuoteScreenBinding.bind(view)
        binding.lifecycleOwner = this
        binding.quote = quote
        binding.quoteMessage.text = "${quote.id}:${quote.message}"
        binding.executePendingBindings()

        val decodedUrl = quote.decodedImageUrl()
        Glide.with(binding.root).load(decodedUrl).into(binding.bgImage)
    }
}