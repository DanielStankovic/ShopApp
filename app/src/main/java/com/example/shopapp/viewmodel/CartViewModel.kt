package com.example.shopapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.application.ShopApplication
import com.example.shopapp.data.ShopDatabase
import com.example.shopapp.model.CartPreview
import com.example.shopapp.model.Order
import com.example.shopapp.model.OrderDetails
import com.example.shopapp.util.RoleHolder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {


    private val shopDatabase = ShopDatabase.getInstance(application as ShopApplication)
    private val cartDao = shopDatabase.cartDao
    private val orderDao = shopDatabase.orderDao

    private val cartEventChannel = Channel<CartEvent>()
    val cartEvent = cartEventChannel.receiveAsFlow()

    val cartPreviewList = cartDao.getCartPreviewList(RoleHolder.currentUserID)

    fun deleteProductFromCart(userID:Int, productID:Int) = viewModelScope.launch{
        val deletedID = cartDao.deleteProductFromCart(userID, productID)
        if(deletedID > 0){
            cartEventChannel.send(CartEvent.ProductDeleted("Proizvod obrisan iz korpe"))

        }else{
            cartEventChannel.send(CartEvent.ErrorProductDeleted("Greška pri brisanju proizvoda iz korpe"))

        }
    }

    fun orderProducts(paymentMethodID:Int, productInCart:List<CartPreview>) = viewModelScope.launch {
        val orderDetailsList = mutableListOf<OrderDetails>()

        //Ovo je objekat porudzbine, ovo je zapravo header.
        val order = Order(userID =  RoleHolder.currentUserID, paymentMethodID = paymentMethodID, dateCreated = System.currentTimeMillis())

        //Prvo vrsimo insert headera da bismo dobili njegov ID
       val insertedOrderID = orderDao.insertOrder(order)

        //prvera da li je to sve insertovano ok. Ako nije ide return i greska
        if(insertedOrderID <= 0){
            cartEventChannel.send(CartEvent.ErrorOrderCreated("Greška pri poručivanju. Pokušajte opet"))
            return@launch
        }

        /*Ovde prolazimo kroz listu proizvoda koji se nalaze u korpi i unacujemo
       i od njih pravimo objekte koji nam trebaju kako bismo napravili detalje porudzbine.
       ID za order tj header dobijamo iz insertovanog Order objekta
        */
        productInCart.forEach { cartPreview -> orderDetailsList.add(
                OrderDetails(
                        orderID = insertedOrderID,
                        productID = cartPreview.productID,
                        quantity = cartPreview.quantity
                )
        ) }

        //Sada kada imamo listu svih orderDetails ide njihov insert
        val numOfInsertedOrderDetails = orderDao.insertAllOrderDetails(orderDetailsList)

        //Provera da li je insert OrderDetailsa prosao kako treba
        if(numOfInsertedOrderDetails.isNotEmpty()){
            //Ako jeste brisemo sve iz korpe
            val numOfDeletedItems = cartDao.deleteOrderedProducts(RoleHolder.currentUserID)
            if(numOfDeletedItems > 0){
                //Znaci da su obrisani uspesno
                cartEventChannel.send(CartEvent.OrderCreated("Porudžbina napravljena uspešno"))
            }else{
                cartEventChannel.send(CartEvent.ErrorOrderCreated("Greška pri poručivanju. Pokušajte opet"))
            }
        }else{
            cartEventChannel.send(CartEvent.ErrorOrderCreated("Greška pri poručivanju. Pokušajte opet"))
        }
    }

    sealed class CartEvent{
        data class ProductDeleted(val message:String) : CartEvent()
        data class ErrorProductDeleted(val errorMessage:String) : CartEvent()
        data class OrderCreated(val createdMessage:String) : CartEvent()
        data class ErrorOrderCreated(val errorCreatedMessage:String) : CartEvent()
    }
}