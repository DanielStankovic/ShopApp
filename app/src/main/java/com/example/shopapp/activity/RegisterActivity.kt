package com.example.shopapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.shopapp.R
import com.example.shopapp.databinding.ActivityRegisterBinding
import com.example.shopapp.viewmodel.AuthenticationViewModel
import com.example.shopapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect

class RegisterActivity : AppCompatActivity() {
    //Isti nacin za dobijanje reference na sve viewove na formi za registraciju. Isto kao kod logina
private lateinit var binding : ActivityRegisterBinding


    /*Ovo je objekat koji koristimo za komunikaciju sa ViewModelom. ViewModel sluzi kako bismo komunicirali sa
        bazom i dobili uvek sveze podatke koje treba da prikazemo na ekranu
        Instancira se dole unutar onCreate metode
         */
    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Ovako se dobija ovaj binding objekat
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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


        //Postavljanje bara na vrhu forme. Ovde postavljamo strelicu za nazad, kao i tekst koji ce se videti u tom bar
        setupActionBar()

        //Postavljanje osluckivaca. Ovde imamo samo osluskivac za dugme za registraciju
        setupListeners()

        lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collect { event ->
                when(event){
                    is AuthenticationViewModel.AuthenticationEvent.SuccessfulRegister -> {
                        //Ovaj event znaci da je registracija prosla kako treba i vracamo korisnika na login ekran
                        Toast.makeText(this@RegisterActivity, event.successMessage, Toast.LENGTH_LONG).show()
                        finish()
                    }
                    is AuthenticationViewModel.AuthenticationEvent.ErrorAuthentication -> {
                        //Znaci da korisnik sa emailom vec postoji u bazi
                        Toast.makeText(this@RegisterActivity, event.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            //Ova linija koda postavlja ikonicu (strelicu) za nazad
            setDisplayHomeAsUpEnabled(true)
            //Ovde postavljamo naslov, tj tekst na vrhu bara. Ovako se dobija string iz strings.xml fajla kao i u Login aktivnosti
            title = resources.getString(R.string.register_title)
        }
    }

    private fun setupListeners() {

        binding.registerBtn.setOnClickListener {

            //Dobijamo tekst iz polja, trimujemo tekst da ne bi imao SPACE (razmak) karaktere ispred i iza teksta
            val nameString = binding.nameEt.text.toString().trim()
            val lastNameString = binding.lastNameEt.text.toString().trim()
            val addressString = binding.addressEt.text.toString().trim()
            val emailString = binding.emailEt.text.toString().trim()
            val passwordString = binding.passwordEt.text.toString().trim()
            val confirmPasswordString = binding.confirmPasswordEt.text.toString().trim()

            //Ovde opet proveravamo polja i postavljamo greske ako su prazna, ili je neka druga greska u pitanju
            if (validateFields(nameString, lastNameString, addressString, emailString, passwordString, confirmPasswordString)) {
                //Znaci da je sve proslo ok i ide dalje logika za registraciju korisnika, tj insert u bazu.
                viewModel.registerUser(nameString, lastNameString, addressString, emailString, passwordString)
            }
        }

    }

    private fun validateFields(nameString:String, lastNameString:String, addressString:String, emailString:String, passwordString:String, confirmPasswordString:String): Boolean {

        //Proveravamo da li je ime polje prazno.
        if(nameString.isEmpty()){
            //Ako je prazno postavljamo gresku na polje. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.nameEt.error = resources.getString(R.string.empty_name)
            return false
        }

        //Proveravamo da li je prezime polje prazno.
        if(lastNameString.isEmpty()){
            //Ako je prazno postavljamo gresku na polje. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.lastNameEt.error = resources.getString(R.string.empty_last_name)
            return false
        }

        //Proveravamo da li je adresa polje prazno.
        if(addressString.isEmpty()){
            //Ako je prazno postavljamo gresku na polje. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.addressEt.error = resources.getString(R.string.empty_address)
            return false
        }

        //Proveravamo da li je email polje prazno.
        if(emailString.isEmpty()){
            //Ako je prazno postavljamo gresku na polje. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.emailEt.error = resources.getString(R.string.empty_email)
            return false
        }

        //Proveravamo da li je lozinka polje prazno.
        if(passwordString.isEmpty()){
            //Ako je prazno postavljamo gresku na polje. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.passwordEt.error = resources.getString(R.string.empty_password)
            return false
        }

        //Proveravamo da li je polje za potvrdu lozinke prazno.
        if(confirmPasswordString.isEmpty()){
            //Ako je prazno postavljamo gresku na polje. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.confirmPasswordEt.error = resources.getString(R.string.empty_confirm_password)
            return false
        }

        //Proveravamo da li je tekst koji je ukucan u email polje odgovarajuceg formata za email
        if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
            //Ako je format emaila pogresan postavljamo gresku na polje. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.emailEt.error = resources.getString(R.string.email_not_valid)
            return false
        }

        //Proveravamo da li je lozinka ima manje od 6 karaktera. Ako ima manje to je greska, mora da ima vise
        if(passwordString.length < 6){
            //Ako lozinka sadrzi manje od 6 karaktera postavljamo gresku. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.passwordEt.error = resources.getString(R.string.short_password)
            return false
        }

        //Proveravamo da li se lozinka i potvrda lozinke isti

        if(!passwordString.equals(confirmPasswordString)){
            //Ako se lozinke ne slazu postavljamo gresku. String za gresku dobijamo iz fajla strings.xml na sledeci nacin.
            binding.confirmPasswordEt.error = resources.getString(R.string.confirm_password_not_match)
            return false
        }

        return true
    }


}