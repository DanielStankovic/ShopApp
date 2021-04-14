package com.example.shopapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.shopapp.R
import com.example.shopapp.adapter.OrderDetailsAdapter
import com.example.shopapp.databinding.ActivityOrderDetailsBinding
import com.example.shopapp.databinding.ActivityProductDetailsBinding
import com.example.shopapp.viewmodel.OrderDetailsViewModel
import com.example.shopapp.viewmodel.ProductDetailsViewModel
import com.example.shopapp.viewmodel.ViewModelFactory

class OrderDetailsActivity : AppCompatActivity() {


    //Ovo je objekat koji koristimo da bi pristupili svim View-ovima (elementima) na login formi.
    //Ranije se koristilo findViewByID za svaki element, ovako ovaj element sadrzi sve viewove koji postoje.
    //Klasa ActivityOrderDetailsBinding se sama generise kada se u projektu u build.gradle stavi opcija viewBinding = true

    private lateinit var binding: ActivityOrderDetailsBinding

    /*Ovo je objekat koji koristimo za komunikaciju sa ViewModelom. ViewModel sluzi kako bismo komunicirali sa
  bazom i dobili uvek sveze podatke koje treba da prikazemo na ekranu
  Instancira se dole unutar onCreate metode
   */
    private lateinit var viewModel: OrderDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Ovako se dobija ovaj binding objekat
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)

        //postavljamo root element bindinga kao contentView, root je ovde zapravo prvi View u hijerarhiji u fajlu activity_login.xml. U ovom slucaju to je LinearLayout
        setContentView(binding.root)

        //Ovo je objekat koji predstavlja aplikaciju. Potreban nam je kako bismo instancirali bazu
        val application = requireNotNull(this).application

        /*ViewModelFactory klase sluze kako bi se uz njiihovu pomoc kreirali viewModeli. Zapravo je nesto kao
         konstruktor za ViewModel. Posto nam u ViewModelu treba referenca ka bazi kako bismo mogli da dobijemo prizvode ili u slucaju admina
         da insertujemo nove proizvode, prosledjujemo application objekat. Application objekat mozemo da iskoristimo da dobijemo bazu unutar viewModela.
        * */
        val viewModelFactory = ViewModelFactory(application)

        /*Ovo je objekat koji koristimo za komunikaciju sa ViewModelom. ViewModel sluzi kako bismo komunicirali sa
        bazom i dobili uvek sveze podatke koje treba da prikazemo na ekranu
        Instancira se dole unutar onCreate metode
         */
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrderDetailsViewModel::class.java)

        val adapter = OrderDetailsAdapter()
        binding.orderDetailsRv.setHasFixedSize(true)
        binding.orderDetailsRv.adapter = adapter


        /*Ovde iz intenta dobijamo ID porudzbine koji smo prosledili iz OrdersFragmenta. String order_id
       je zapravo kljuc pod kojim smo vrednost prosledili, zato taj isti kljuc koristimo
       ovde da bismo dobili vrednost. Drugi parametar je default vrednost, u slucaju da
       se ne pronadje vrednost koja je pod ovim kljucem
        */
        val orderID = intent.getIntExtra("order_id", -1)


        /*Ovde postavljamo bar na vrhu forme. Prosledjuejmo ID proudzbine kako bismo ga priakzali gore
         */
        setupActionBar(orderID)

        //Ovde u viewModel postavljamo ID odabrane porudzbine. Posle unutar viewModel filtriramo
        //na osnovu ovog ID-a
        viewModel.setOrderID(orderID)

        viewModel.orderDetailsList.observe(this) {
            it?.let {
                adapter.submitList(it)
            }
        }

    }

    private fun setupActionBar(orderID: Int) {
        supportActionBar?.apply {
            //Ova linija koda postavlja ikonicu (strelicu) za nazad
            setDisplayHomeAsUpEnabled(true)
            //Ovde postavljamo naslov, tj tekst na vrhu bara.
            title = "Porudžbina: $orderID"
        }
    }
}