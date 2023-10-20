package com.abhishek.quotes.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.abhishek.quotes.R
import com.abhishek.quotes.domain.model.network.ResponseStatus
import com.abhishek.quotes.ui.auth.LoginActivity
import com.abhishek.quotes.ui.auth.LoginViewModel
import com.abhishek.quotes.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val splashViewModel by viewModels<SplashViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        attachObservers()
    }

    private fun attachObservers() {
        lifecycleScope.launch {

            launch {
                splashViewModel.adminData.collectLatest {
                    if (it.token.isNullOrBlank()) {
                        // send to login screen
                        sendToActivity(LoginActivity::class.java)
                    } else {
                        // validate if valid token
                        loginViewModel.login(username = it.username, token = it.token)
                    }
                }
            }

            launch {
                loginViewModel.loginResponseStatus.collectLatest { status ->
                    when (status) {
                        ResponseStatus.FETCHING,
                        ResponseStatus.UNDEFINED -> Unit
                        ResponseStatus.FETCHED -> sendToActivity(HomeActivity::class.java)
                        is ResponseStatus.FAILURE -> {
                            Toast.makeText(this@SplashActivity, "Session expired!", Toast.LENGTH_SHORT).show()
                            sendToActivity(LoginActivity::class.java)
                        }
                    }
                }
            }
        }
    }

    private fun sendToActivity(klass: Class<*>) {
        startActivity(Intent(this, klass))
        finish()
    }
}