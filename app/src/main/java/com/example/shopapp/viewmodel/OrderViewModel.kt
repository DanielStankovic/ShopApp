package com.example.shopapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopapp.application.ShopApplication
import com.example.shopapp.data.ShopDatabase

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val orderDao = ShopDatabase.getInstance(application as ShopApplication).orderDao

    val orderList = orderDao.getOrderList()

}