package com.example.loginmvvm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginmvvm.Repositoy.SharedPreRepository

class SharedPrefViewModelFactory(private val sharedPreRepository: SharedPreRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedPrefViewModel(sharedPreRepository) as T
    }
}