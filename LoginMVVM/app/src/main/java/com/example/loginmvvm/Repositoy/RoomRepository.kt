package com.example.loginmvvm.Repositoy

import com.example.loginmvvm.db.userDAO
import com.example.loginmvvm.model.UserDetailsDbModel

class RoomRepository(private val userDAO: userDAO) {

    suspend fun verifyUser(emailId:String, password:String): UserDetailsDbModel {
        return userDAO.verifyUser(emailId,password)
    }

    suspend fun addUser(userDetails: UserDetailsDbModel){
        userDAO.insertUserData(userDetails)
    }
    suspend fun deleteUser(userName: String){
        userDAO.deleteUser(userName)
    }

    suspend fun isDbEmpty():Int{
        return userDAO.isDbEmpty()
    }

//    suspend fun getUser():UserDetails{
//        return userDAO.getUser
//    }
}