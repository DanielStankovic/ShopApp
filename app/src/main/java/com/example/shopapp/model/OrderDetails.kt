package com.example.shopapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderDetails(
        @PrimaryKey(autoGenerate = true) val orderDetailsID: Int = 0,
        @ColumnInfo(name = "order_id") val orderID: Long,
        @ColumnInfo(name = "product_id") val productID:Int,
        @ColumnInfo(name = "quantity") val quantity:Int
)
