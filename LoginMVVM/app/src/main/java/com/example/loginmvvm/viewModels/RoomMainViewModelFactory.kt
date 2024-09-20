package com.example.loginmvvm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loginmvvm.Repositoy.RoomRepository

class RoomMainViewModelFactory(private val roomRepository: RoomRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RoomMainViewModel(roomRepository) as T
    }
}