package com.example.loginmvvm.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginmvvm.Repositoy.GoogleRepository
import com.example.loginmvvm.model.UserDetailsModel
import kotlinx.coroutines.launch

class GoogleSignInViewModel(private val repository: GoogleRepository):ViewModel() {
    private val _userDetails = MutableLiveData<UserDetailsModel>()
    val userDetails: LiveData<UserDetailsModel> get() = _userDetails

    fun googleSignIn(onResult:(UserDetailsModel)->Unit){
        viewModelScope.launch {
            val result = repository.googleSignIn()  // No need to use `withContext`
            Log.d("HomeViewModel", "Sign-in result: $result")
            _userDetails.postValue(result)
            onResult(result)
        }
    }

    fun signOut(onComplete:()->Unit){
        viewModelScope.launch {
            repository.signOut()
            _userDetails.postValue(UserDetailsModel("","","",false))
            onComplete()
        }
    }

}