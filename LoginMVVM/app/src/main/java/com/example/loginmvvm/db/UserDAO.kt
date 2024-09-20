package com.example.loginmvvm.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.loginmvvm.model.UserDetailsDbModel

@Dao
interface UserDAO {
    @Query("SELECT * FROM userDetails WHERE emailId = :emailId AND password= :password ")
    suspend fun verifyUser(emailId:String,password:String):UserDetailsDbModel

    @Insert
    suspend fun insertUserData(userDetail: UserDetailsDbModel)

    @Query("DELETE FROM userDetails WHERE Name = :emailId")
    suspend fun deleteUser(emailId: String)

    @Query("SELECT COUNT(*) FROM userDetails")
    suspend fun isDbEmpty(): Int

//    @Query("SELECT * FROM userDetails")
//    suspend fun getUser():UserDetails




}