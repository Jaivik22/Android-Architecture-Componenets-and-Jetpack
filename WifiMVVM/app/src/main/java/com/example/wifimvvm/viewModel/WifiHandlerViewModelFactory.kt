package com.example.wifimvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wifimvvm.repository.WifiHandlerRepository

class WifiHandlerViewModelFactory(private val repository: WifiHandlerRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WifiHandlerViewModel(repository) as T
    }
}