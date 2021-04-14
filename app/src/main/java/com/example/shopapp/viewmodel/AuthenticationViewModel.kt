package com.example.shopapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopapp.application.ShopApplication
import com.example.shopapp.data.ShopDatabase
import com.example.shopapp.model.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {


    /*Ovo je DAO objekat. DAO je interface koji sluzi za komunikaciju sa Room bazom podataka.
    U tom interfejsu se definisu sve metode koje ce odredjena tabela da ima. U trenutnom slucaju
    UserDao ima metode koje su nam potrebne kako bismo dobili usera iz baze, ili insertovali novog usera
     */
    private val userDao = ShopDatabase.getInstance(application as ShopApplication).userDao
    private val authEventChannel = Channel<AuthenticationEvent>()
    val loginEvent = authEventChannel.receiveAsFlow()

    fun loginUser(emailString: String, passwordString: String) = viewModelScope.launch {

        /*--------------NAPOMENA-----------
        * u DAO interfejsu postoje metode koje ispred imena imaju kljucnu rec SUSPEND. Ove metode su
        * metode kojima treba neko vreme da se izvrse. Kako korisniku ne bi zakocio ekran dok se te metode izvrsavaju,
        * one se pokrecu u posebnoj niti koja se jos zove i kao radnik nit. Ova nit U POZADINI izvrsava sve sto je potrebno i
        * vraca rezultat kada je gotov. Kako bismo pokrenuli SUSPEND funkciju, moramo da iskoristimo ovaj deo koda viewModelScope.launcg{}
        * (deo koda koji stoji nakon imena funkcije i paraemtara gore). Na taj nacin ce viewModel da ocita SUSPEND funkciju i da nam vrati rezultat,
        * u trenutnom slucaju vratice nam korisnika iz baze. Svaka komunikacija sa bazom je u vecini slucaja oznacena sa SUSPEND metodom.
        * */

        //Ovde pozivamo metodu iz DAO interfejsa za login korisnika.
        val user = userDao.loginUser(emailString, passwordString)

        if (user != null) {
            //Znaci da je login prosao kako treba i da postoji korisnik sa ovim kredencijalima u bazi
            authEventChannel.send(
                AuthenticationEvent.SuccessfulAuthentication(
                    user,
                    "Uspešan login"
                )
            )

        } else {
            //Znaci da ne postoji korisnik sa ovim kredencijalima u bazi
            authEventChannel.send(AuthenticationEvent.ErrorAuthentication("Korisnik sa ovim kredencijalima ne postoji u bazi."))

        }
    }

    fun registerUser(
        nameString: String,
        lastNameString: String,
        addressString: String,
        emailString: String,
        passwordString: String
    ) = viewModelScope.launch {

        /*Ovde vrsimo insert novog korisnika u bazu. Ali pre toga, potrebno je da proverimo da li korisnik sa
        unetim emailom vec postoji u bazi. Posto i ovde imamo SUSPEND funckije, moramo da koristimo viewModelScope.launch kako bismo pokrenuli metodu
        * */
        val userName = userDao.getUserByEmail(emailString)

        if (userName.isNullOrBlank()) {
            //Znaci da ne postoji korisnik sa ovim emailom i ide insert u bazu
           val insertedUserID = userDao.insert(
                User(
                    name = nameString,
                    lastName = lastNameString,
                    address = addressString,
                    email = emailString,
                    password = passwordString,
                    roleID = 1
                )
            )
            if(insertedUserID > 1){
            authEventChannel.send(
                AuthenticationEvent.SuccessfulRegister(
                    "Uspešana registracija"
                )
            )
            }else{
                authEventChannel.send(AuthenticationEvent.ErrorAuthentication("Došlo je do greške pri registraciji korisnika."))
            }
        } else {
            //Znaci da postoji korisnik sa ovim emailom i ide greska
            authEventChannel.send(AuthenticationEvent.ErrorAuthentication("Korisnik sa ovim emailom već postoji u bazi."))
        }
    }


    sealed class AuthenticationEvent {
        data class SuccessfulAuthentication(val user: User, val message: String) :
            AuthenticationEvent()
        data class ErrorAuthentication(val errorMessage: String) : AuthenticationEvent()
        data class SuccessfulRegister(val successMessage: String) : AuthenticationEvent()
    }

}
