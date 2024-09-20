package com.example.loginmvvm.Repositoy

import com.example.loginmvvm.db.UserSharedPrefrences

class SharedPreRepository(private val userSharedPrefrences: UserSharedPrefrences) {

    fun getUserName():String{
        return userSharedPrefrences.getUserName()
    }

    fun isGoogleUser():Boolean{
        return userSharedPrefrences.isGoogleUser()
    }

    fun clearSharedPreferences(){
        userSharedPrefrences.clearSharedPreferences()
    }

    fun putUserName(userName:String){
        userSharedPrefrences.putUserName(userName)
    }
    fun addGoogleUserStatus(flag:Boolean){
        userSharedPrefrences.addGoogleUserStatus(flag)
    }
}