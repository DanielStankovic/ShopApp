package com.example.shopapp.activity

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.shopapp.databinding.ActivityAddProductBinding
import com.example.shopapp.viewmodel.AddProductViewModel
import com.example.shopapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect


class AddProductActivity : AppCompatActivity() {

    //Ovo je objekat koji koristimo da bi pristupili svim View-ovima (elementima) na login formi.
    //Ranije se koristilo findViewByID za svaki element, ovako ovaj element sadrzi sve viewove koji postoje.
    //Klasa ActivityAddProductBinding se sama generise kada se u projektu u build.gradle stavi opcija viewBinding = true
    private lateinit var binding: ActivityAddProductBinding

    /*Ovo je objekat koji koristimo za komunikaciju sa ViewModelom. ViewModel sluzi kako bismo komunicirali sa
      bazom i dobili uvek sveze podatke koje treba da prikazemo na ekranu
      Instancira se dole unutar onCreate metode
       */
    private lateinit var viewModel: AddProductViewModel

    //Ovo je varijabla za ID prodavnice. U onCreate cemo da je setujemo, tj dobijamo je iz Intent objekta
    private var storeID = -1

    private val REQUEST_CODE = 100

    private var isImageSelected = false

    private lateinit var imageBitmap : Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //Ovako se dobija ovaj binding objekat
        binding = ActivityAddProductBinding.inflate(layoutInflater)

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
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddProductViewModel::class.java)

        /*Ovde iz intenta dobijamo ID radnje koji smo prosledili iz ProductActivity. String store_id
        je zapravo kljuc pod kojim smo vrednost prosledili, zato taj isti kljuc koristimo
        ovde da bismo dobili vrednost. Drugi parametar je default vrednost, u slucaju da
        se ne pronadje vrednost koja je pod ovim kljucem
         */
        storeID = intent.getIntExtra("store_id", -1)


        /*Ovde postavljamo bar na vrhu forme. Upisujemo naslov unutar ove metode
        */
        setupActionBar()

        //Postavljanje osluskivaca za klik na dugme kada se dodaje artikal
        setupListener()

        //Ovde postavljamo osluskivac kako bismo dobijali Evente tj rezultate onoga sto je odradjeno u viewModelu
        lifecycleScope.launchWhenStarted {
            viewModel.addProductEvent.collect { event ->
                when(event){
                    is AddProductViewModel.AddProductEvent.ProductAdded -> {
                        Toast.makeText(this@AddProductActivity, event.message, Toast.LENGTH_LONG).show()
                        this@AddProductActivity.finish()
                    }
                    is AddProductViewModel.AddProductEvent.ErrorProductAdded ->  {
                        Toast.makeText(this@AddProductActivity, event.errorMessage, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

    private fun setupListener() {
        binding.saveProductBtn.setOnClickListener {
            //Dobijamo tekst iz polja, trimujemo tekst da ne bi imao SPACE (razmak) karaktere ispred i iza teksta
            val nameString = binding.productNameEt.text.toString().trim()
            val priceString = binding.productPriceEt.text.toString().trim()
            val descriptionString = binding.productDescEt.text.toString().trim()

            //Ovde opet proveravamo polja i postavljamo greske ako su prazna, ili je neka druga greska u pitanju
            if (validateFields(nameString, priceString, descriptionString)) {

                viewModel.saveProduct(storeID, nameString, priceString.toInt(), descriptionString, imageBitmap)
            }
        }

        binding.addProductIv.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private fun validateFields(
            nameString: String,
            priceString: String,
            descriptionString: String
    ): Boolean {

        //Proveravamo da li je ime polje prazno.
        if (!isImageSelected) {
           Toast.makeText(this, "Slika je obavezna", Toast.LENGTH_SHORT).show()
            return false
        }

        //Proveravamo da li je ime polje prazno.
        if (nameString.isEmpty()) {

            binding.productNameEt.error = "Ime je obavezno!"
            return false
        }

        //Proveravamo da li je cena polje prazno.
        if (priceString.isEmpty()) {
            //Ako je prazno postavljamo gresku na polje.
            binding.productPriceEt.error = "Cena je obavezna!"
            return false
        }

        //Proveravamo da li je opis polje prazno.
        if (descriptionString.isEmpty()) {
            //Ako je prazno postavljamo gresku na polje.
            binding.productDescEt.error = "Opis je obavezan"
            return false
        }

        //Proveravamo da li je cena manja ili jednaka nuli. Ako jeste ide greska
        if (priceString.toInt() <= 0) {
            //Korisnik je uneo vrednsot manju od nule ili nula
            binding.productPriceEt.error = "Cena morabiti veća od 0!"
            return false
        }

        return true

    }


    private fun setupActionBar() {
        supportActionBar?.title = "Dodavanje novog artikla"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            isImageSelected = true
            val dataUri = data?.data

             imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, dataUri)
            Glide.with(this).load(dataUri).into(binding.addProductIv)
        }
    }
}