package com.example.shopapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity (primaryKeys = ["store_id", "product_id"])
data class StoreProductRelation(
        @ColumnInfo(name = "store_id")  val storeID: Int,
        @ColumnInfo(name = "product_id")  val productID: Int,
)
