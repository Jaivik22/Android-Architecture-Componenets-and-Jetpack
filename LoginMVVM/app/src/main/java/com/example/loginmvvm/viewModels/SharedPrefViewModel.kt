package com.example.loginmvvm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginmvvm.Repositoy.SharedPreRepository
import kotlinx.coroutines.launch

class SharedPrefViewModel(private val sharedPreRepository: SharedPreRepository):ViewModel() {

    fun getUserName(onResult: (String)->Unit){
        viewModelScope.launch {
            val result = sharedPreRepository.getUserName()
            onResult(result)
        }
    }

    fun isGoogleUser(onResult: (Boolean) -> Unit){
        viewModelScope.launch {
            val result = sharedPreRepository.isGoogleUser()
            onResult(result)
        }
    }

    fun clearSharedPreferences(){
        viewModelScope.launch {
            sharedPreRepository.clearSharedPreferences()
        }
    }

    fun putUserName(userName:String){
        viewModelScope.launch {
            sharedPreRepository.putUserName(userName)
        }
    }

    fun addGoogleUserStatus(flag:Boolean){
        viewModelScope.launch {
            sharedPreRepository.addGoogleUserStatus(flag)
        }
    }
}