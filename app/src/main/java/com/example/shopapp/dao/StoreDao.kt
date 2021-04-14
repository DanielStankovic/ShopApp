package com.example.shopapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shopapp.model.Store

@Dao
interface StoreDao {

    @Insert
    suspend fun insertStores(data : List<Store>)

    @Query("SELECT * FROM Store")
    fun getStoreList() : LiveData<List<Store>>
}