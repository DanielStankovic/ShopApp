package com.example.shopapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(
        @PrimaryKey(autoGenerate = true) val orderID: Int = 0,
        @ColumnInfo(name = "user_id") val userID: Int,
        @ColumnInfo(name = "payment_method_id") val paymentMethodID: Int,
        @ColumnInfo(name = "date_created") val dateCreated: Long
)
