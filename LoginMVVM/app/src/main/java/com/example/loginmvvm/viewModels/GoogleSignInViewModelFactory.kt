package com.example.loginmvvm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginmvvm.Repositoy.GoogleRepository

class GoogleSignInViewModelFactory(private val repository: GoogleRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GoogleSignInViewModel(repository) as T
    }
}