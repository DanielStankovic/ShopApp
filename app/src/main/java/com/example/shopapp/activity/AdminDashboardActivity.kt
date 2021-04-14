package com.example.shopapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopapp.R
import com.example.shopapp.databinding.ActivityAdminDashboardBinding
import com.example.shopapp.fragment.OrdersFragment
import com.example.shopapp.fragment.ShoppingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/*Ova aktivnost sadrzi navigacioni meni na dnu. Klikom na elemente u navigacionom meniju otvaraju se fragmenti koji su zapravo forme
Postoje 2 forme
1. Forma za pregled prodavnice i artikala
2. Pregled porudzbenica koje je korisnik napravio
* */
class AdminDashboardActivity : AppCompatActivity() {
    //Ovo je objekat koji koristimo da bi pristupili svim View-ovima (elementima) na login formi.
    //Ranije se koristilo findViewByID za svaki element, ovako ovaj element sadrzi sve viewove koji postoje.
    //Klasa ActivityAdminDashboardBinding se sama generise kada se u projektu u build.gradle stavi opcija viewBinding = true
    private lateinit var binding : ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Ovako se dobija ovaj binding objekat
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)


        //postavljamo root element bindinga kao contentView, root je ovde zapravo prvi View u hijerarhiji u fajlu activity_login.xml. U ovom slucaju to je LinearLayout
        setContentView(binding.root)

        //Ovde postavljamo osluskivac za kliktanje na elemente u navigavionom meniju
        binding.bottomNavigationView.setOnNavigationItemSelectedListener (mOnNavigationItemSelectedListener)

        /*Posto prvi put kada udjemo na formu ni jedan element nije kliknut automatski moramo da ovde kroz kod
        postavimo neki fragment i odaberemo element.
         */

        val fragment = ShoppingFragment()
        supportFragmentManager.beginTransaction().replace(R.id.adminDashboardFrameLayout, fragment)
                .commit()
        supportActionBar?.title = "Pregled"
    }

    /*Ovo je logika koja se desava kada se klikne na elemente u navigacionom meniju. menuItem je zapravo element koji je kliknut
   i onda na osnovu njegovog ID-a proveravamo i postavljamo fragmente u container koji ih prikazuje. Container je u ovom slucaju userDashboardFrameLayout.
   On se nalazi u layoutu ove aktivnosti. Deo za promenu fragmenta je ovaj dole gde se poziva supportFragmentManager pa transkacija pa replace. U replace metodi
   prvi parametar je container koji ce da "drzi" framgent a drugi parametar je zapravo fragment koji ce da se prikaze. Ovaj listener zahteva da se vrati true ili false
   u zavisnosti da li je sve ok proslo, tj fragment se postavio. Zato stavljamo true kada god vratimo fragment ili false ako se desi da nismo odabrali element, sto je
   zapravo nemoguce da se desi
    */
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigation_overview -> {
                val fragment = ShoppingFragment()
                supportFragmentManager.beginTransaction().replace(R.id.adminDashboardFrameLayout, fragment)
                        .commit()
                supportActionBar?.title = "Pregled"
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_orders -> {
                val fragment = OrdersFragment()
                supportFragmentManager.beginTransaction().replace(R.id.adminDashboardFrameLayout, fragment)
                        .commit()
                supportActionBar?.title = "Porudžbine"
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}