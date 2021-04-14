package com.example.shopapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    //Ovo polje koristimo kako bismo znali da li je logovan Admin ili Korisnik.
    //Admin ima vrednost 0 a korisnik vrednost 1
    @ColumnInfo(name = "roleId") val roleID: Int

)
