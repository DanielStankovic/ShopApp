package com.example.shopapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopapp.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM User WHERE email = :email AND password = :password ")
    suspend fun loginUser(email:String, password:String) : User?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User) : Long


    @Query("SELECT name FROM User WHERE email = :emailString")
    suspend fun getUserByEmail(emailString: String): String?
}