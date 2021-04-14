package com.example.shopapp.data

import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.shopapp.application.ShopApplication
import com.example.shopapp.dao.*
import com.example.shopapp.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [User::class, Store::class, Product::class,
    StoreProductRelation::class, Cart::class, Order::class,
    OrderDetails::class], version = 1, exportSchema = false)
abstract class ShopDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val storeDao: StoreDao
    abstract val productDao: ProductDao
    abstract val cartDao: CartDao
    abstract val orderDao: OrderDao

    companion object {

        @Volatile
        private var INSTANCE: ShopDatabase? = null

        fun getInstance(shopApplication: ShopApplication): ShopDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                            shopApplication.applicationContext,
                            ShopDatabase::class.java,
                            "shop_database"
                    )
                            .fallbackToDestructiveMigration()
                            .addCallback(DatabaseInitialSetup(shopApplication.applicationScope))
                            .build()
                    INSTANCE = instance
                }

                /*Ovaj deo je jako bitan. Room baza se ne kreira sve dok se ne izvrsi neka operacija nad njom. Posto mi kada se kreira
                aplikacija ubacujemo odredjene podatke u bazu, potrebno nam je da se baza kreira cim se aplikacija upali po prvi put. Kako
                bismo pokrenuli to kreiranje izvrsimo prost upit da bi se ona kreirala a zatim i napunila. Posto je objekat instance SINGLETON(postoji
                samo jedan u citavoj aplikaciji) ovo ce se desiti samo prvi put kada se pokrene aplikacija.
                 */
                instance.query("SELECT 1", null)
                return instance
            }
        }
    }

    private class DatabaseInitialSetup (private val scope : CoroutineScope) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            ShopDatabase.INSTANCE?.let {
                shopDatabase ->
                scope.launch {
                    val userDao = shopDatabase.userDao
                    val storeDao = shopDatabase.storeDao
                    val productDao = shopDatabase.productDao
                    val adminUser = User(name = "Admin", lastName = "Admin", address = "Local", email = "admin@gmail.com", password = "admin", roleID = 0)
                    val testUser = User(name = "Daniel", lastName = "Stankovic", address = "LocalTest", email = "a@gmail.com", password = "a", roleID = 1)
                    userDao.insert(adminUser)
                    userDao.insert(testUser)

                    /*Ovde vrsimo insert svih radnji kada se kreira baza. Ovaj objekat listOfStores je zapravo lista svih ranji koju smo rucno kreirali
                    Ova lista nalazi se unutar foldera data pa fajl InitalDbData.kt.
                     */
                    storeDao.insertStores(listOfStores)

                    /*Ovde vrsimo insert svih proizvoda kada se kreira baza. Ovaj objekat listOfProducts je zapravo lista svih artikla koju smo rucno kreirali
                   Ova lista nalazi se unutar foldera data pa fajl InitalDbData.kt.
                    */
                    productDao.insertProducts(listOfProducts)

                    /*Ovde vrsimo insert veza izmedju prodavnica i artikala. Ovaj objekat listOfStoreProductRelations je zapravo lista relacija koju smo rucno kreirali
                  Ova lista nalazi se unutar foldera data pa fajl InitalDbData.kt.
                  */
                    productDao.insertStoreProductRelations(listOfStoreProductRelations)

                }
            }

        }
    }
}


