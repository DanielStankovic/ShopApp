package com.example.shopapp.model

import androidx.room.ColumnInfo

data class OrderPreview(
        @ColumnInfo(name = "order_id") val orderID : Int,
        @ColumnInfo(name = "user_name") val userName : String,
        @ColumnInfo(name = "method_payment") val methodPayment : Int,
        @ColumnInfo(name = "total_price") val totalPrice : Int,
        @ColumnInfo(name = "date_created") val dateCreated : Long
)
