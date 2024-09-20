package com.example.loginmvvm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginmvvm.Repositoy.RoomRepository
import com.example.loginmvvm.model.UserDetailsDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomMainViewModel(private val repository: RoomRepository) :ViewModel(){
    fun verifyUser(email: String, password: String, onResult: (UserDetailsDbModel?) -> Unit) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.verifyUser(email, password)
            }
            onResult(result)
        }
    }

    fun addUser(user: UserDetailsDbModel, onComplete: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.addUser(user)
            }
            onComplete()
        }
    }

    fun deleteUser(user: String?, onComplete: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (user != null) {
                    repository.deleteUser(user)
                }
            }
            onComplete()
        }
    }

    fun isDbEmpty(onResult: (Int) -> Unit){
        viewModelScope.launch {
           val result =  withContext(Dispatchers.IO){
                repository.isDbEmpty()
            }
            onResult(result)
        }
    }

//    fun getUser(onResult: (UserDetails) -> Unit){
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO){
//                repository.getUser()
//            }
//            onResult(result)
//        }
//    }
}