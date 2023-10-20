package com.abhishek.quotes.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.quotes.Admin
import com.abhishek.quotes.data.network.AdminLoginAPI
import com.abhishek.quotes.data.proto.adminDataStore
import com.abhishek.quotes.domain.model.network.AuthResponse
import com.abhishek.quotes.domain.model.network.ResponseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val adminLoginAPI: AdminLoginAPI,
): AndroidViewModel(application) {

    private val _loginResponseStatus = MutableStateFlow<ResponseStatus>(ResponseStatus.UNDEFINED);
    val loginResponseStatus = _loginResponseStatus.asStateFlow()

    fun login(username: String, token: String) {
        viewModelScope.launch {
            val response = adminLoginAPI.login(username, token)
            val code = response.code()
            val responseBody: AuthResponse? = response.body()

            if (code == 201 && responseBody != null) {
                val newToken = responseBody.token
                if (!newToken.isNullOrBlank()){
                    saveTokenAndUsernameInDatastore(
                        username = username,
                        token = newToken,
                    )
                }

                _loginResponseStatus.update { ResponseStatus.FETCHED }
            }else {
                val errorMessage = responseBody?.message ?: "Response code : $code"
                _loginResponseStatus.update { ResponseStatus.FAILURE(message = errorMessage) }
            }
        }
    }

    private suspend fun saveTokenAndUsernameInDatastore(username: String, token: String) {
        application.adminDataStore.updateData {
            Admin.newBuilder().setToken(token).setUsername(username).build()
        }
    }

}