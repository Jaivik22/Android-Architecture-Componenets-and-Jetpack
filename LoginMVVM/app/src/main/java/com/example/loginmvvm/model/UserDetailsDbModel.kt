package com.example.loginmvvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userDetails")
data class UserDetailsDbModel (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val Name:String,
    val emailId:String,
    val password:String,
    val googlesignIn:Boolean
    )