package com.example.shopapp.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.application.ShopApplication
import com.example.shopapp.data.ShopDatabase
import com.example.shopapp.model.Product
import com.example.shopapp.model.StoreProductRelation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class AddProductViewModel(application: Application) : AndroidViewModel(application) {
    private val productDao = ShopDatabase.getInstance(application as ShopApplication).productDao
    private val applicationContext = application.applicationContext

    private val addProductEventChannel = Channel<AddProductEvent>()
    val addProductEvent = addProductEventChannel.receiveAsFlow()

    fun saveProduct(storeID: Int, productName: String, productPrice: Int, productDescription: String, bitmapImage: Bitmap) = viewModelScope.launch {

        try {
            var file = File(applicationContext.filesDir, "Images")
            if (!file.exists()) {
                file.mkdir()
            }
            val imageName = "${System.currentTimeMillis()}_added_art.jpg"
            file = File(file, imageName)
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()

            //Kako bismo kreirali ID proizvoda moramo da vidimo koliki je max broj ID-a koji je vec u bazi i da povecamo za jedan.
            val currentMaxProductID = productDao.getMaxProductID()
            //Ovde povecavamo za jedan da dobijemo novi ID za proizvod
            val productID = currentMaxProductID + 1

            val product = Product(productID, productName, productDescription, productPrice, imageName, false)
            val insertedID = productDao.insertProduct(product)
            //Ovde proveravamo da li je product insertovan
            if (insertedID > 0) {
                //Znaci da jeste i onda insertujemo ID ovog proizvoda u relacionu tabelu, kako bi ovaj artikal bio vidljiv u ovoj radnji
                val insertedRelationID = productDao.insertStoreProductRelation(StoreProductRelation(storeID, productID))
                //Proveravamo da li je relacija uspesno isnertovana
                if (insertedRelationID > 0) {
                    //Znaci da je sve uspesno proslo
                    addProductEventChannel.send(AddProductEvent.ProductAdded("Proizvod uspešno dodat!"))
                } else {
                    addProductEventChannel.send(AddProductEvent.ErrorProductAdded("Greška pri dodavanju proizvoda! Pokušajte ponovo"))
                }
            } else {
                addProductEventChannel.send(AddProductEvent.ErrorProductAdded("Greška pri dodavanju proizvoda! Pokušajte ponovo"))

            }


        } catch (e: Exception) {
            addProductEventChannel.send(AddProductEvent.ErrorProductAdded("Greška pri dodavanju proizvoda! Greška: ${e.message}"))
            e.printStackTrace()
        }
    }

    sealed class AddProductEvent {
        data class ProductAdded(var message: String) : AddProductEvent()
        data class ErrorProductAdded(var errorMessage: String) : AddProductEvent()
    }

}