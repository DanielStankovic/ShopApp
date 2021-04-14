package com.example.shopapp.application

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ShopApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

}