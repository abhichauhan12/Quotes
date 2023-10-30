package com.abhishek.quotes.ui.home.quote.edit

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abhishek.quotes.R
import com.abhishek.quotes.databinding.FragmentAddQuoteBinding
import com.abhishek.quotes.domain.model.network.ResponseStatus
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class EditQuote : Fragment(R.layout.fragment_add_quote) {

    private val editQuotesViewModel: EditQuotesViewModel by viewModels()
    private lateinit var binding: FragmentAddQuoteBinding
    private var selectedImageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        attachObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(view: View) {
        binding = FragmentAddQuoteBinding.bind(view)
        binding.apply {
            backButton.setOnClickListener { findNavController().navigateUp() }
            toolbarTitle.text = "Edit Quote"
            selectImage.setOnClickListener {
                // Launch the photo picker and let the user choose only images.
                pickMediaContract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            submitQuote.setOnClickListener { validateQuoteAndSubmit() }
        }
        fillQuoteData()
    }

    private fun fillQuoteData() {
        val quote = EditQuoteArgs.fromBundle(requireArguments()).quote
        binding.apply {
            quoteMessage.setText(quote.message)
            quoteAuthor.setText(quote.author)
            quoteLanguage.setText(quote.language)
            Glide.with(this.root).load(quote.decodedImageUrl()).into(quoteBackgroundImage)
        }
    }

    private fun attachObservers() {
        lifecycleScope.launch {

            launch {
                editQuotesViewModel.editQuoteStatus.collectLatest {
                    when (it) {
                        ResponseStatus.UNDEFINED -> Unit
                        ResponseStatus.FETCHING -> {
                            Toast.makeText(requireContext(), "Uploading...", Toast.LENGTH_SHORT)
                                .show()
                            binding.submitQuote.isEnabled = false
                        }

                        ResponseStatus.FETCHED -> findNavController().navigateUp()
                        is ResponseStatus.FAILURE -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            binding.submitQuote.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun validateQuoteAndSubmit() {
        val imageFile = getImageFileFromUri()
        val quoteMessage = binding.quoteMessage.text.trim().toString()
        val quoteAuthor = binding.quoteAuthor.text.trim().toString()
        val quoteLanguage = binding.quoteLanguage.text.trim().toString()

        val originalQuote = EditQuoteArgs.fromBundle(requireArguments()).quote
        editQuotesViewModel.editQuote(originalQuote.id, quoteMessage, quoteAuthor, quoteLanguage, imageFile)
    }

    private fun getImageFileFromUri(): File? {
        if (selectedImageUri == null) return null

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            requireContext().contentResolver.query(selectedImageUri!!, projection, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            return File(filePath)
        }
        return null
    }

    // Registers a photo picker activity launcher in single-select mode.
    private val pickMediaContract =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                selectedImageUri = uri
                binding.quoteBackgroundImage.setImageURI(selectedImageUri!!)
                Toast.makeText(requireContext(), selectedImageUri.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
}