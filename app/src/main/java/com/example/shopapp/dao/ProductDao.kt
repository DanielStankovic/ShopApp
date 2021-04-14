package com.example.shopapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopapp.model.Product
import com.example.shopapp.model.StoreProductRelation

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProducts(data:List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product) : Long

    @Insert
    suspend fun insertStoreProductRelations(data:List<StoreProductRelation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoreProductRelation(storeProductRelation: StoreProductRelation) : Long

    @Query("SELECT p.id, p.name, p.description, p.image_name, p.price, CASE WHEN c.product_id IS NULL THEN 0 ELSE 1 END as is_added_to_cart FROM Product p INNER JOIN StoreProductRelation spr ON spr.product_id = p.id LEFT JOIN Cart c ON c.product_id = p.id AND c.user_id = :userID WHERE spr.store_id = :storeID")
    fun getProductList(storeID:Int, userID:Int) : LiveData<List<Product>>

    @Query("SELECT MAX(id) FROM Product")
    suspend fun getMaxProductID() : Int
}