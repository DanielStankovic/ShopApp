package com.example.shopapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.shopapp.R
import com.example.shopapp.databinding.ActivityLoginBinding
import com.example.shopapp.util.RoleHolder
import com.example.shopapp.viewmodel.AuthenticationViewModel
import com.example.shopapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect


class LoginActivity : AppCompatActivity() {

    //Ovo je objekat koji koristimo da bi pristupili svim View-ovima (elementima) na login formi.
    //Ranije se koristilo findViewByID za svaki element, ovako ovaj element sadrzi sve viewove koji postoje.
    //Klasa ActivityLoginBinding se sama generise kada se u projektu u build.gradle stavi opcija viewBinding = true
    private lateinit var binding : ActivityLoginBinding


    /*Ovo je objekat koji koristimo za komunikaciju sa ViewModelom. ViewModel sluzi kako bismo komunicirali sa
      bazom i dobili uvek sveze podatke koje treba da prikazemo na ekranu
      Instancira se dole unutar onCreate metode
       */
    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //Ovako se dobija ovaj binding objekat
        binding = ActivityLoginBinding.inflate(layoutInflater)

        //postavljamo root element bindinga kao contentView, root je ovde zapravo prvi View u hijerarhiji u fajlu activity_login.xml. U ovom slucaju to je LinearLayout
        setContentView(binding.root)

        //Ovo je objekat koji predstavlja aplikaciju. Potreban nam je kako bismo instancirali bazu
        val application = requireNotNull(this).application

        /*ViewModelFactory klase sluze kako bi se uz njiihovu pomoc kreirali viewModeli. Zapravo je nesto kao
         konstruktor za ViewModel. Posto nam u ViewModelu treba referenca ka bazi kako bismo mogli da dobijemo Usera ili da insertujemo novog usera
        prosledjujemo application objekat. Application objekat mozemo da iskoristimo da dobijemo bazu unutar viewModela.
        * */
        val viewModelFactory = ViewModelFactory(application)

        /*Ovo je objekat koji koristimo za komunikaciju sa ViewModelom. ViewModel sluzi kako bismo komunicirali sa
        bazom i dobili uvek sveze podatke koje treba da prikazemo na ekranu
        Instancira se dole unutar onCreate metode
         */
         viewModel = ViewModelProvider(this, viewModelFactory).get(AuthenticationViewModel::class.java)


        //Postavljanje naslova na vrhu forme
        setupActionBar()

        //Postavljanje osluckivaca za slucanje klika na teskt registrujte Se i na dugme za login.
        setupListeners()

        //Ovde postavljamo osluskivac kako bismo dobijali Evente tj rezultate onoga sto je odradjeno u viewModelu
        lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collect {event ->
                when(event){
                    is AuthenticationViewModel.AuthenticationEvent.SuccessfulAuthentication -> {
                        //Ovaj event znaci da je login prosao i ovde prikazujemo tu poruku koju smo prosledili iz ViewModela
                        Toast.makeText(this@LoginActivity, event.message, Toast.LENGTH_LONG).show()

                        //Iz eventa dobijamo i user objekat koji mozemo da iskoristimo
                        val user = event.user
                        //Ovde proveravamo da li je korisnik koji se logovao Admin ili User preko roleID-a. 0 je Admin 1 je User
                        val roleID = user.roleID
                        /*Ovde postavljamo trenutnu rolu na globalnom nivou, kako bismo mogli posle u
                        fragmentima da proveravamo koja je rola logovana i da razdvojimo logiku za admina i usera
                         */

                        RoleHolder.currentRoleID = roleID
                        RoleHolder.currentUserID = user.id
                        //Ovde u zavisnosti od RoleID pravimo intent za otvarnje aktivnsi za Admina ili Usera
                        val intent =  if(roleID == 0) Intent(this@LoginActivity, AdminDashboardActivity::class.java) else Intent(this@LoginActivity, UserDashboardActivity::class.java)
                        //Ovde ubacujemo ID korisnika i ime. ID usera ce nam trebati kasnije kada pravimo porudzbenice ili dodajemo artikle kao Admin
                        intent.putExtra("userName", user.name)
                        intent.putExtra("userId", user.id)
                        startActivity(intent)
                    }
                    is AuthenticationViewModel.AuthenticationEvent.ErrorAuthentication -> {
                        //Ovde znaci da login nije prosao i prikazujemo poruku sa greskom
                        Toast.makeText(this@LoginActivity, event.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            //Ovde postavljamo naslov, tj tekst na vrhu bara. Ovako se dobija string iz strings.xml fajla kao i u Login aktivnosti
            title = resources.getString(R.string.login_title)
        }
    }

    private fun setupListeners() {
        //Elementima iz layouta pristupamo preko IDa koji smo im dodelili na sledeci nacin. Ukoliko nekom view-u nismo dodelili ID (kao na primer TextView "Nemate nalog"
        //on se nece ni prikazati kada ukucamo binding.
        binding.loginBtn.setOnClickListener {

            //Dobijamo tekst iz polja, trimujemo tekst da ne bi imao SPACE (razmak) karaktere ispred i iza teksta
            val emailString = binding.emailEt.text.toString().trim()
            val passwordString = binding.passwordEt.text.toString().trim()

            //Ovde prvo vrsimo validaciju za polja za email i password
            if (validateFields(emailString, passwordString)) {

                //Ovde znaci da je sve proslo ok, sva polja su ok i ide dalje logika za login korisnika

                    //Ovde u ViewModel prosledjujemo podatke za login
                viewModel.loginUser(emailString, passwordString)

            }
        }

        binding.registerTv.setOnClickListener(View.OnClickListener {

            //Ovde je korisnik kliknuo na tekst "REgustrujte se!" i otvaramo novu aktivnost za registraciju

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        })
    }

    private fun validateFields(emailString : String, passwordString : String): Boolean {

        //Svaki put kada nesto nije u redu mi vracamo FALSE, kako uslov u prethodnoj medoti ne bi prosao

        //Proveravamo da li je email polje prazno.
        if(emailString.isEmpty()){
            //Ako je prazno postavljamo gresku na polje. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.emailEt.error = resources.getString(R.string.empty_email)
            return false
        }

        //Proveravamo da li je password polje prazno.
        if(passwordString.isEmpty()){
            //Ako je prazno postavljamo gresku na polje. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.passwordEt.error = resources.getString(R.string.empty_password)
            return false
        }

        //Proveravamo da li je tekst koji je ukucan u email polje odgovarajuceg formata za email
        if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
            //Ako je format emaila pogresan postavljamo gresku na polje. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.emailEt.error = resources.getString(R.string.email_not_valid)
            return false
        }

        return true
    }


}