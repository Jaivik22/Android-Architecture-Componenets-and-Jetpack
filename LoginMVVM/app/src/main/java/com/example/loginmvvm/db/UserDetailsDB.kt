package com.example.loginmvvm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.loginmvvm.model.UserDetailsDbModel

@Database(entities = [UserDetailsDbModel::class], version = 1)
abstract class UserDetailsDB:RoomDatabase() {
    abstract fun userDao():userDAO

    companion object{
        private var INSTANCE:UserDetailsDB?=null
        fun getDatabase(context:Context):UserDetailsDB{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context,
                        UserDetailsDB::class.java,
                        "userDetails_database"
                    )
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}