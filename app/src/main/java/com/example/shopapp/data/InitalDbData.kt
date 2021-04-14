package com.example.shopapp.data

import com.example.shopapp.R
import com.example.shopapp.model.Product
import com.example.shopapp.model.Store
import com.example.shopapp.model.StoreProductRelation


//Ovde definisemo listu svih radnji koje cemo da insertujemo kada se baza kreira
val listOfStores = listOf(

    Store(
        1,
        "eStovarište",
        "Preko 8.000 proizvoda za gradnju, renoviranje i uređenje Vašeg doma ili bašte",
        "Stovarište",
        "radnja_1"
    ),

    Store(
        2,
        "Stovarište Jakovljević",
        "Brza, laka i sigurna online kupovina iz udobnosti Vašeg doma",
        "Stovarište",
        "radnja_2"
    ),

    Store(
        3,
        "Mixal alati",
        "Najpoznatiji brendovi alata na jednom mestu.",
        "Alati",
        "radnja_3"
    ),

    Store(
        4,
        "VolimSvojDom.rs",
        "Svi alati za Vaš dom na jednom mestu",
        "Alati",
        "radnja_4"

    ),

    Store(
        5,
        "Dijaspora Shop",
        "Na klik do savršene majstorije",
        "Mašine",
        "radnja_5"
    ),

    Store(
        6,
        "Gama",
        "Prodaja i ovlašćeni servis za veliki broj brendova",
        "Mašine",
        "radnja_6"

    )
)

val listOfProducts = listOf(
    Product(
        1,
        "KLIMABLOC 30 – Zorka opeka",
        "Glineni blok sa poboljšanim termičkim karakteristikama, pogodan je za gradnju " +
                "energetski efikasnih objekata",
        126,
        "artikal_1"
    ),

    Product(
        2,
        "Ventilacioni blok VB-16 – Mladost Leskovac",
        "Glineni ventilacioni blok za izradu ventilacionih kanala.",
        86,
        "artikal_2"
    ),

    Product(
        3,
        "Toza Marković – Crep Premium Kikinda",
        "Keramički crep za pokrivanje kosih krovova minimalnog nagiba 22°",
        57,
        "artikal_3"
    ),

    Product(
        4,
        "Cigla - Puna opeka",
        "Puna opeka se tradicionalno koristi za sve tipove građevina i zidova. Odlikuje je velika pritisna čvrstoća i širina primene",
        20,
        "artikal_4"
    ),

    Product(
        5,
        "Daska 48mm – 5m",
        "Čamova građa. Širina dasaka varira (od 10 do 30cm) radi orjentacije, u 1m3 staje oko 40m2 daske. To znači da vam za 1m2 pokrivanja treba 0,025m3 daske",
        1000,
        "artikal_5"
    ),

    Product(
        6,
        "Ceresit CT84 Express",
        "Za pričvršćivanje EPS-ploča (ploča od ekspandiranog polistirena), na fasadne zidove u svrhu termo izolacije građevine koristeći nisko vlažnu metodu.",
        916,
        "artikal_6"
    ),

    Product(
        7,
        "Ceresit CT 83 – 25kg – lepak za stiropor",
        "Lepak za polistirenske ploče (EPS, stiropor), odlična adhezija na mineralne površine i polistirenske ploče, otporan na smrzavanje i vremenske uticaje,  paropropustan, brzo postizanje čvrstoće.",
        485,
        "artikal_7"
    ),

    Product(
        8,
        "RÖFIX P50 staklena mrežica-50m",
        "Staklenu mrežicu za armiranje umetnuti u armirajući sloj, uz najmanji preklop na spojevima od 10 cm i temeljito zagladiti ravnom stranom gletera.",
        5160,
        "artikal_8"
    ),

    Product(
        9,
        "Austrotherm Stiropor EPS AF – 4cm",
        "zolacija fasadnih zidova (tzv. “demit fasada“). Izbor debljine ploče mora biti na osnovu termičkog proračuna. Ugradnja se vrši lepljenjm uz dodatno tiplovanje prema uputstvu. ",
        1121,
        "artikal_9"
    ),

    Product(
        10,
        "Paljena žica – 1.2mm / 20kg",
        "Paljena (žarena) žica velike savitljivosti. Najčešće se koristi prilikom armiračkih radova",
        2600,
        "artikal_10"
    ),

    Product(
        11,
        "Klešta papagaj – industrial 250 mm",
        "Hrom Vanadium – CrV. Crna završna obrada. Dvokomponentna plastična ručka",
        926,
        "artikal_11"
    ),

    Product(
        12,
        "Klešta armiračka professional",
        "Klešta za armiračke radove, povijanja i vezivanja žice",
        2465,
        "artikal_12"
    ),

    Product(
        13,
        "Klešta za armaturu 30″ HIB-30",
        "Posebno konstruisana klešta za sečenje armature. Omogućavaju olakšano sečenje zbog posebnog oblika čeljusti i ugla otvaranja",
        8750,
        "artikal_13"
    ),

    Product(
        14,
        "Ključevi imbus 9/1 zvezdasti",
        "Imbus set sa 9kom hrom vanadijum ključeva zvezdastog preseka. Set sadrži T10, T15, T20, T25, T27, T30, T40, T45 i T50 mm ključeve",
        671,
        "artikal_14"
    ),

    Product(
        15,
        "Francuski ključ sa gumiranom drškom 25cm",
        "Ključ univerzalne namene sa podesivim mehanizmom za otvaranje čeljusti. Gumirana drška za udobno rukovanje",
        1118,
        "artikal_15"
    ),

    Product(
        16,
        "Četka special 20-70mm – drvena drška",
        "Četka višestruke namene sa drvenom drškom. Pogodna za molerske i farbarske radove i za namaze na uljanoj i nitro bazi.",
        193,
        "artikal_16"
    ),

    Product(
        17,
        "Valjak vestan beli – komplet 25 cm/49mm",
        "Valjak namenjen nanosu disperzija i poludisperzija. Prečnik valjka: 49mm. Dužina valjka: 25cm",
        386,
        "artikal_17"
    ),

    Product(
        18,
        "Sekira tesarska 0.80 kg",
        "Tesarska sekira sa čeličnom glavom i drvenom drškom",
        2055,
        "artikal_18"
    ),

    Product(
        19,
        "Macola drvena drška – 5000gr",
        "Macola sa čeličnom glavom i drvenom drškom",
        4290,
        "artikal_19"
    ),
    Product(
        20,
        "Ručna testera 500mm – drvena drška",
        "Ručna testera za sečenje drveta. Telo od čelika sa drvenom drškom",
        815,
        "artikal_20"
    ),

    Product(
        21,
        "Makita Aku odvijač – 3,6V",
        "Koristi se linijski ili u obliku pištolja. Ugrađeno LED radno svetlo. Prekidač za promenu smera",
        8731,
        "artikal_21"
    ),

    Product(
        22,
        "Makita – Akumulator 2Ah",
        "Tip akumulatora: Li-ion, Napon 12V, kapacitet: 2Ah",
        5689,
        "artikal_22"
    ),

    Product(
        23,
        "Laser za merenje rastojanja Makita LD050P",
        "Merenje pojedinačnog rastojanja, površina, zapremina, Pitagora (2 i 3 tačke) i za obeležavanje. Za pokrivače krovova i izvođače gips kartona, arhitekte, molere, električare, montere kuhinja itd.",
        15381,
        "artikal_23"
    ),
    Product(
        24,
        "Jednoručna glodalica Makita – 3709 – 530W",
        "Štitnik za kabl, montiran u gornjem delu i napojni kabl omogućuju lako držanje i rukovanje. Mehanizam sa zupčastom letvom za fino podešavanje dubine",
        15000,
        "artikal_24"
    ),
    Product(
        25,
        "Set burgija i umetaka – Makita 102 kom",
        "Nasadni ključ:5-11,5/32“,7/32“,1/4“,9/32“,5/16“,3/8“,7/16“. šestougaoni umetak: 3, 4, 5; 60 mm magnetni umeci",
        5985,
        "artikal_25"
    ),

    Product(
        26,
        "Makita – Čekić-bušilica HR2810 – 800W",
        "3 radna režima – rotacija, rotacija sa udaranjem, udaranje. Ergonomski dizajnirana drška, sa gumiranim rukohvatom",
        51290,
        "artikal_26"
    ),

    Product(
        27,
        "Makita – AKU. Bušilica-odvijač – DDF482Z",
        "Brzostezna glava omogućuje lako postavljanje i uklanjanje umetka. Metalna konstrukcija svih zupčanika",
        19560,
        "artikal_27"
    ),

    Product(
        28,
        "Makita – Aku. Lančana testera – DUC254Z",
        "Idealna za korišćenje u javnim prostorijama ili u stambenoj zoni, sa niskom emisijom buke. Režim pojačanja obrtnog momenta privremeno pojačava graničnik struje za povećanje snage za rezanje debljih grana",
        36520,
        "artikal_28"
    ),

    Product(
        29,
        "Makita Ekscetrična brusilica – 310W",
        "Odvojiva prednja drška je pogodna za ugaono brušenje. Drška i kućište motora su ergonomski redizajnirani",
        37896,
        "artikal_29"
    ),

    Product(
        30,
        "Makita Ravna brusilica – 400W",
        "Motorni deo otporan na prašinu sa cik-cak zaštitom. Otvori za ventilaciju su dizajnirani za direktno izduvavanje vazduha sa suprotne strane od korisnika",
        19846,
        "artikal_30"
    ),


    )


val listOfStoreProductRelations = listOf(
        StoreProductRelation(1, 1),
        StoreProductRelation(1, 2),
        StoreProductRelation(1, 3),
        StoreProductRelation(1, 4),
        StoreProductRelation(1, 5),
        StoreProductRelation(1, 6),
        StoreProductRelation(1, 7),

        StoreProductRelation(2, 2),
        StoreProductRelation(2, 3),
        StoreProductRelation(2, 4),
        StoreProductRelation(2, 6),
        StoreProductRelation(2, 7),
        StoreProductRelation(2, 8),
        StoreProductRelation(2, 9),
        StoreProductRelation(2, 10),

        StoreProductRelation(3, 11),
        StoreProductRelation(3, 12),
        StoreProductRelation(3, 13),
        StoreProductRelation(3, 14),
        StoreProductRelation(3, 15),
        StoreProductRelation(3, 16),

        StoreProductRelation(4, 15),
        StoreProductRelation(4, 16),
        StoreProductRelation(4, 17),
        StoreProductRelation(4, 18),
        StoreProductRelation(4, 19),
        StoreProductRelation(4, 20),

        StoreProductRelation(5, 21),
        StoreProductRelation(5, 22),
        StoreProductRelation(5, 23),
        StoreProductRelation(5, 24),
        StoreProductRelation(5, 25),
        StoreProductRelation(5, 26),
        StoreProductRelation(5, 27),
        StoreProductRelation(5, 28),
        StoreProductRelation(5, 29),
        StoreProductRelation(5, 30),

        StoreProductRelation(6, 21),
        StoreProductRelation(6, 22),
        StoreProductRelation(6, 24),
        StoreProductRelation(6, 27),
        StoreProductRelation(6, 29),
        StoreProductRelation(6, 30),

)