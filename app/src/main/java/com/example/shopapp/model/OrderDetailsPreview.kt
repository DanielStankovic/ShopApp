package com.example.shopapp.model

import androidx.room.ColumnInfo

data class OrderDetailsPreview (
    @ColumnInfo(name = "quantity") val quantity : Int,
    @ColumnInfo(name = "product_name") val productName : String,
    @ColumnInfo(name = "product_price") val productPrice : Int,
    @ColumnInfo(name = "image_name") val imageName : String
)