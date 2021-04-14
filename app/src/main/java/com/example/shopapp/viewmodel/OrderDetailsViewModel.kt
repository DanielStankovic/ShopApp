package com.example.shopapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.shopapp.application.ShopApplication
import com.example.shopapp.data.ShopDatabase

class OrderDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val orderDao = ShopDatabase.getInstance(application as ShopApplication).orderDao
    private val orderIDMutableLiveData = MutableLiveData<Int>()

    val orderDetailsList = Transformations.switchMap(orderIDMutableLiveData){
        orderID ->
        orderDao.getOrderDetailsList(orderID)
    }

    fun setOrderID(orderID:Int){
        orderIDMutableLiveData.value = orderID
    }
}