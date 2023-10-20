package com.abhishek.quotes.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.abhishek.quotes.R
import com.abhishek.quotes.databinding.ActivityLoginBinding
import com.abhishek.quotes.domain.model.network.ResponseStatus
import com.abhishek.quotes.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initViews()
        attachObservers()
    }

    private fun initViews() {
        binding.login.setOnClickListener {
            val username = binding.username.text.toString().trim()
            loginViewModel.login(username = username, token = "")
        }
    }

    private fun attachObservers() {
        lifecycleScope.launch {
            launch {
                loginViewModel.loginResponseStatus.collectLatest { status ->
                    when(status) {
                        ResponseStatus.UNDEFINED -> Unit
                        ResponseStatus.FETCHING -> {
                            binding.login.isEnabled = false
                            binding.loader.visibility = View.VISIBLE
                        }
                        ResponseStatus.FETCHED -> {
                            sendToHomeActivity()
                        }
                        is ResponseStatus.FAILURE -> {
                            binding.login.isEnabled = true
                            binding.loader.visibility = View.GONE
                            Toast.makeText(this@LoginActivity, status.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun sendToHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}