package com.example.shopapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity (primaryKeys = ["user_id", "product_id"])
data class Cart(
    @ColumnInfo(name = "user_id") val userID : Int,
    @ColumnInfo(name = "product_id") val productID : Int,
    @ColumnInfo(name = "quantity") val quantity : Int,
    @ColumnInfo(name = "time_added") val timeAdded : Long
)
