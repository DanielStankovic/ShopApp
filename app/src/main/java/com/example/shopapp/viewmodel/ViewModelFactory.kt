package com.example.shopapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthenticationViewModel(application) as T
        }else if(modelClass.isAssignableFrom(StoreViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return StoreViewModel(application) as T
        }
        else if(modelClass.isAssignableFrom(ProductViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(application) as T
        }
        else if(modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ProductDetailsViewModel(application) as T
        }
        else if(modelClass.isAssignableFrom(CartViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(application) as T
        }
        else if(modelClass.isAssignableFrom(OrderViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(application) as T
        }
        else if(modelClass.isAssignableFrom(OrderDetailsViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return OrderDetailsViewModel(application) as T
        }
        else if(modelClass.isAssignableFrom(AddProductViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AddProductViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}