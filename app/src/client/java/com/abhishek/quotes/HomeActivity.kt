package com.abhishek.quotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.abhishek.quotes.data.network.QuoteFetchAPI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject lateinit var fetchAPI: QuoteFetchAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        CoroutineScope(Dispatchers.IO + Job()).launch {
            val result = fetchAPI.getQuotes(viewedQuoteIds = "", selectedLanguages = "").body()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeActivity, result, Toast.LENGTH_LONG).show()
            }
        }
    }
}