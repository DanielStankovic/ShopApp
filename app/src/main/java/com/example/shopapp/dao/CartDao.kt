package com.example.shopapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shopapp.model.Cart
import com.example.shopapp.model.CartPreview

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductToCart(data: Cart) : Long

    @Query("SELECT quantity FROM Cart WHERE product_id=:productID AND user_id=:userID")
    fun getQuantityInCartForProduct(productID:Int, userID:Int) : LiveData<Int>

    @Query("SELECT c.product_id, c.user_id, c.quantity, p.name as product_name, p.price as product_price, p.image_name FROM Cart c INNER JOIN Product p ON p.id = c.product_id WHERE c.user_id=:userID ORDER BY c.time_added DESC")
    fun getCartPreviewList(userID:Int) : LiveData<List<CartPreview>>

    @Query("DELETE FROM Cart WHERE user_id=:userID AND product_id=:productID")
    suspend fun deleteProductFromCart(userID:Int, productID:Int) : Int

    @Query("DELETE FROM Cart WHERE user_id=:userID")
    suspend fun deleteOrderedProducts(userID:Int) : Int
}