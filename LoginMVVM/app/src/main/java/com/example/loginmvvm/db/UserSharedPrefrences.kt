package com.example.loginmvvm.db

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class UserSharedPrefrences(private val context: Context) {
    var sharedPrefrences: SharedPreferences
    lateinit var editor: Editor
    init {
        sharedPrefrences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)

    }

    fun getUserName():String{
        return sharedPrefrences.getString("UserName","").toString()
    }

    fun addGoogleUserStatus(flag:Boolean){
        editor = sharedPrefrences.edit()
        editor.putBoolean("GoogleSignIn",flag)
        editor.apply()
    }
    fun isGoogleUser():Boolean{
        return sharedPrefrences.getBoolean("GoogleSignIn",false)
    }

    fun clearSharedPreferences(){
        sharedPrefrences.edit().clear()
    }
    fun putUserName(userName:String){
        editor = sharedPrefrences.edit()
        editor.putString("UserName",userName)
        editor.apply()
    }


}