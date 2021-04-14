package com.example.shopapp.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shopapp.adapter.ProductAdapter
import com.example.shopapp.databinding.ActivityProductBinding
import com.example.shopapp.model.Product
import com.example.shopapp.util.RoleHolder
import com.example.shopapp.viewmodel.ProductViewModel
import com.example.shopapp.viewmodel.ViewModelFactory
import android.util.Pair as UtilPair

class ProductActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {

    //Ovo je objekat koji koristimo da bi pristupili svim View-ovima (elementima) na login formi.
    //Ranije se koristilo findViewByID za svaki element, ovako ovaj element sadrzi sve viewove koji postoje.
    //Klasa ActivityProductBinding se sama generise kada se u projektu u build.gradle stavi opcija viewBinding = true
    private lateinit var binding : ActivityProductBinding

    /*Ovo je objekat koji koristimo za komunikaciju sa ViewModelom. ViewModel sluzi kako bismo komunicirali sa
      bazom i dobili uvek sveze podatke koje treba da prikazemo na ekranu
      Instancira se dole unutar onCreate metode
       */
    private lateinit var viewModel : ProductViewModel

    //Ovo je varijabla za ID prodavnice. U onCreate cemo da je setujemo, tj dobijamo je iz Intent objekta
    private var storeID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Ovako se dobija ovaj binding objekat
        binding = ActivityProductBinding.inflate(layoutInflater)

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
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductViewModel::class.java)

        /*Ovde iz intenta dobijamo ID radnje koji smo prosledili iz ShoppingFramgenta. String store_id
        je zapravo kljuc pod kojim smo vrednost prosledili, zato taj isti kljuc koristimo
        ovde da bismo dobili vrednost. Drugi parametar je default vrednost, u slucaju da
        se ne pronadje vrednost koja je pod ovim kljucem
         */
        storeID = intent.getIntExtra("store_id", -1)

        //Ovde u viewModel postavljamo ID odabrane prodavnice. Posle unutar viewModel filtriramo
        //na osnovu ovog ID-a
        viewModel.setStoreID(storeID)

        /*Ovde postavljamo ime radnje na vrh forme, isto kao u ostalim aktivnostima sto postaljamo naslov
        Posto je ime string a ne int kao sto je storeID gore, za string ne mora default vrednost posto je
        default vrednost null. Zato u metodi setupActionBar proveramo da li je null
         */
        setupActionBar(intent.getStringExtra("store_name"))

        //Ovde postavljamo osluskivac za kliktanje na dugme za dodavanje artikla
        setupListeners()

        val adapter= ProductAdapter(this)
        binding.productRv.adapter = adapter
        binding.productRv.setHasFixedSize(true)

        //Skrivanje dugmeta za dodavanje artikla ako je korisnik u pitanju
        toggleAddProductBtn()

        viewModel.productList.observe(this){
            it.let {
                adapter.submitList(it)
            }
        }

    }

    private fun setupListeners() {
        binding.addProductBtn.setOnClickListener{
            //Ovde otvaramo formu za dodavanje artikla. Prosledjujemo ID radnje da kada ga dodamo u bazu mozemo da ga vezemo za ovu radnju
            val intent = Intent(this, AddProductActivity::class.java)
            intent.putExtra("store_id", storeID)
            startActivity(intent)
        }

    }

    private fun setupActionBar(storeName: String?) {
        if(storeName!= null){
            supportActionBar?.apply {
                //Ova linija koda postavlja ikonicu (strelicu) za nazad
                setDisplayHomeAsUpEnabled(true)
                //Ovde postavljamo naslov, tj tekst na vrhu bara.
                title = storeName
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleAddProductBtn(){
        binding.addProductBtn.visibility = if(RoleHolder.currentRoleID == 0) View.VISIBLE else View.GONE
    }

    override fun onItemClick(product: Product,
                             productImage:ImageView,
                             productName: TextView,
                             productDesc:TextView,
                             productPrice:TextView) {

        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("selectedProduct", product)
        val options = ActivityOptions.makeSceneTransitionAnimation(this,
            UtilPair.create(productImage, "productImage"),
            UtilPair.create(productName, "productName"),
            UtilPair.create(productDesc, "productDesc"),
            UtilPair.create(productPrice, "productPrice"))
        startActivity(intent, options.toBundle())
    }
}