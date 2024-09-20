package com.example.loginmvvm.Repositoy

import com.example.loginmvvm.model.UserDetailsModel
import com.example.loginmvvm.utils.GoogleSignInClient


class GoogleRepository(val googleSignInClient: GoogleSignInClient) {
    suspend fun googleSignIn(): UserDetailsModel {
        val userDetails = googleSignInClient.googleSignIn()
//        Log.d("UserRepository",flag.toString())
        return userDetails
    }

    fun signOut(){
        googleSignInClient.signOut()
    }
}