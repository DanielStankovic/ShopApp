package com.example.shopapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopapp.model.Order
import com.example.shopapp.model.OrderDetails
import com.example.shopapp.model.OrderDetailsPreview
import com.example.shopapp.model.OrderPreview

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrder(order: Order) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllOrderDetails(orderDetailsList:List<OrderDetails>) : List<Long>

    @Query("SELECT o.orderID as order_id, o.payment_method_id as method_payment, o.date_created as date_created, u.name as user_name, SUM(p.price * od.quantity) as total_price  FROM `Order` o INNER JOIN User u ON u.id = o.user_id INNER JOIN OrderDetails od ON od.order_id = o.orderID INNER JOIN Product p ON p.id = od.product_id GROUP BY o.orderID, o.payment_method_id, o.date_created, u.name HAVING SUM(p.price * od.quantity) > 0 ORDER BY o.date_created ASC")
    fun getOrderList() : LiveData<List<OrderPreview>>

    @Query("SELECT od.quantity as quantity, p.name as product_name, p.price as product_price, p.image_name as image_name FROM OrderDetails od INNER JOIN Product p ON p.id = od.product_id  WHERE od.order_id=:orderID ORDER BY p.name")
    fun getOrderDetailsList(orderID:Int) : LiveData<List<OrderDetailsPreview>>
}