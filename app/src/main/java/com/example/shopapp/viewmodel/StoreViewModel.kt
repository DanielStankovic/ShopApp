package com.example.shopapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shopapp.application.ShopApplication
import com.example.shopapp.dao.StoreDao
import com.example.shopapp.data.ShopDatabase

class StoreViewModel(application: Application) : AndroidViewModel(application){

    /*Ovo je DAO objekat. DAO je interface koji sluzi za komunikaciju sa Room bazom podataka.
   U tom interfejsu se definisu sve metode koje ce odredjena tabela da ima. U trenutnom slucaju
   storeDao ima metode koje su nam potrebne kako bismo dobili sve prodacnice iz baze
    */
    private val storeDao = ShopDatabase.getInstance(application as ShopApplication).storeDao

    val storeList = storeDao.getStoreList()
}

