package com.example.shopapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.shopapp.application.ShopApplication
import com.example.shopapp.data.ShopDatabase
import com.example.shopapp.util.RoleHolder

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    /*Ovo je DAO objekat. DAO je interface koji sluzi za komunikaciju sa Room bazom podataka.
  U tom interfejsu se definisu sve metode koje ce odredjena tabela da ima. U trenutnom slucaju
  product ima metode koje su nam potrebne kako bismo dobili sve artikle iz baze
   */
    private val productDao = ShopDatabase.getInstance(application as ShopApplication).productDao
    private val storeIDMutLiveData = MutableLiveData<Int>()

    val productList = Transformations.switchMap(storeIDMutLiveData){
        storeID ->
        productDao.getProductList(storeID, RoleHolder.currentUserID)
    }

    fun setStoreID (storeID:Int){
        storeIDMutLiveData.value = storeID
    }
}