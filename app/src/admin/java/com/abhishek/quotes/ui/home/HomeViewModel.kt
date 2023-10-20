package com.abhishek.quotes.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.abhishek.quotes.Admin
import com.abhishek.quotes.data.proto.adminDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application
): AndroidViewModel(application) {

    val adminData : Flow<Admin> = application.adminDataStore.data
}