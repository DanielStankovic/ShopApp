package com.example.shopapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.shopapp.databinding.ActivityProductDetailsBinding
import com.example.shopapp.model.Product
import com.example.shopapp.util.RoleHolder
import com.example.shopapp.viewmodel.ProductDetailsViewModel
import com.example.shopapp.viewmodel.ProductViewModel
import com.example.shopapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import java.io.File
import java.text.DecimalFormat

class ProductDetailsActivity : AppCompatActivity() {

    //Ovo je objekat koji koristimo da bi pristupili svim View-ovima (elementima) na login formi.
    //Ranije se koristilo findViewByID za svaki element, ovako ovaj element sadrzi sve viewove koji postoje.
    //Klasa ActivityProductDetailsBinding se sama generise kada se u projektu u build.gradle stavi opcija viewBinding = true

    private lateinit var binding : ActivityProductDetailsBinding

    /*Ovo je objekat koji koristimo za komunikaciju sa ViewModelom. ViewModel sluzi kako bismo komunicirali sa
    bazom i dobili uvek sveze podatke koje treba da prikazemo na ekranu
    Instancira se dole unutar onCreate metode
     */
    private lateinit var viewModel : ProductDetailsViewModel

    private lateinit var selectedProduct: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Ovako se dobija ovaj binding objekat
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)

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
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductDetailsViewModel::class.java)



         selectedProduct = (intent.getSerializableExtra("selectedProduct") as Product?)!!
        /*Ovde postavljamo podatke o artiklu koji je kliknu. Taj artikal smo prosledili
        * kroz intent*/
        setupProductData(selectedProduct)

        /*Postavljanje IDja selektovanog artikla. Ovo nam treba kako bismo iz baze iz tabele Cart
       dobili koja je kolicina za ovaj proizvod dodata
        */
        viewModel.setProductID(selectedProduct.id)

        //Moramo u View koji sluzi za odabiranje kolicine da postavimo min i maks vrednost
        setupNumberPicker()

        /*Ovde postavljamo ime radnje na vrh forme, isto kao u ostalim aktivnostima sto postaljamo naslov
       Posto je ime string a ne int kao sto je storeID gore, za string ne mora default vrednost posto je
       default vrednost null. Zato u metodi setupActionBar proveramo da li je null
        */
        setupActionBar(selectedProduct.name)

        //Postavljanje osluskivaca za klik na dugme dodaj u korpu
        setupListeners()

        //Ovde postavljamo osluskivac kako bismo dobijali Evente tj rezultate onoga sto je odradjeno u viewModelu
        lifecycleScope.launchWhenStarted {
            viewModel.productDetailsEvent.collect {
                event ->
                when(event){
                    is ProductDetailsViewModel.ProductDetailsEvent.ProductAdded -> {
                        //Ovaj event znaci da je proizvod dodat uspesno
                        Toast.makeText(this@ProductDetailsActivity, event.message, Toast.LENGTH_LONG).show()
                    }
                    is ProductDetailsViewModel.ProductDetailsEvent.ErrorProductAdded -> {
                        //Ovaj event znaci da je doslo do greski pri dodavanju
                        Toast.makeText(this@ProductDetailsActivity, event.errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewModel.getQuantityInCart.observe(this){quantity ->
           if(quantity == null){
               binding.apply {
                   productDetailsPicker.value = 1
                   productDetailsAddedQtyTv.text = "Kol. u korpi: 0"
                   productDetailsTotalPriceTv.text = "Ukupno: 0.00"
               }
           }else{
               binding.apply {
                   productDetailsPicker.value = quantity
                   productDetailsAddedQtyTv.text = "Kol. u korpi: $quantity"
                   productDetailsTotalPriceTv.text = "Ukupno: ${DecimalFormat("#,###.00").format((selectedProduct.price.toDouble() * quantity))}"

               }
           }

        }

        //Skrivanje elemenata ako se logovao ADMIN. Na layout fajlu smo napravili grupu i dodali
        //IDjeve elmeenata u tu grupu. Sada je doboljno da ovde smao hidujemo grupu i svi ce biti hidovani
        //0 je za admina 1 je za korisnika
        if(RoleHolder.currentRoleID == 0) binding.hideViewGroup.visibility = View.INVISIBLE else binding.hideViewGroup.visibility = View.VISIBLE
    }

    private fun setupListeners() {
        binding.productDetailsAddBtn.setOnClickListener {
            viewModel.addProductToCart(selectedProduct.id, RoleHolder.currentUserID, binding.productDetailsPicker.value)
        }
    }


    private fun setupActionBar(productName:String?) {
        if(productName!= null){
            supportActionBar?.apply {
                //Ovde postavljamo naslov, tj tekst na vrhu bara.
                title = productName
            }
        }
    }

    private fun setupProductData(selectedProduct: Product?) {
       binding.apply {
           selectedProduct?.let { prod ->
               productDetailsNameTv.text = prod.name
               productDetailsDescTv.text = prod.description
               productDetailsPriceTv.text = "${DecimalFormat("#,###.00").format(prod.price.toDouble())} RSD"
               if(selectedProduct.imageName.startsWith("artikal")) {
                   val imageResourceID = resources.getIdentifier(selectedProduct.imageName, "drawable", packageName)
                   Glide.with(this@ProductDetailsActivity).load(imageResourceID).into(productDetailsIv)
               }else{
                   val imagesDirectory = File(application.applicationContext.filesDir, "Images")
                   val imageFile = File(imagesDirectory, selectedProduct.imageName)
                   Glide.with(this@ProductDetailsActivity).load(imageFile).into(productDetailsIv)
               }
           }
       }
    }

    private fun setupNumberPicker() {
        binding.productDetailsPicker.minValue = 1
        binding.productDetailsPicker.maxValue = 100
    }
}