package com.example.shopapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.shopapp.application.ShopApplication
import com.example.shopapp.data.ShopDatabase
import com.example.shopapp.model.Cart
import com.example.shopapp.util.RoleHolder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class ProductDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val cartDao = ShopDatabase.getInstance(application as ShopApplication).cartDao

    private val prodDetailsEventChannel = Channel<ProductDetailsEvent>()
    val productDetailsEvent = prodDetailsEventChannel.receiveAsFlow()
    private val productIDMutableLiveData = MutableLiveData<Int>()

    val getQuantityInCart = Transformations.switchMap(productIDMutableLiveData){
            productID -> cartDao.getQuantityInCartForProduct(productID, RoleHolder.currentUserID)
    }

    fun addProductToCart(productID:Int, userID:Int, quantity: Int) = viewModelScope.launch{
        val insertedID = cartDao.addProductToCart(Cart(userID, productID, quantity, System.currentTimeMillis()))
        if(insertedID > 0){
            prodDetailsEventChannel.send(ProductDetailsEvent.ProductAdded("Proizvod dodat u korpu!"))
        }else{
            prodDetailsEventChannel.send(ProductDetailsEvent.ErrorProductAdded("Greška pri dodavanju proizvoda u korpu!"))
        }
    }

    fun setProductID(productID:Int){
        productIDMutableLiveData.value = productID
    }



    sealed class ProductDetailsEvent{
        data class ProductAdded(val message:String) : ProductDetailsEvent()
        data class ErrorProductAdded(val errorMessage:String) : ProductDetailsEvent()
    }

}