package com.example.shopapp.model

import androidx.room.ColumnInfo

data class CartPreview(
        @ColumnInfo(name = "user_id") val userID : Int,
        @ColumnInfo(name = "product_id") val productID : Int,
        @ColumnInfo(name = "quantity") val quantity : Int,
        @ColumnInfo(name = "product_name") val productName : String,
        @ColumnInfo(name = "product_price") val productPrice : Int,
        @ColumnInfo(name = "image_name") val imageName : String
)
