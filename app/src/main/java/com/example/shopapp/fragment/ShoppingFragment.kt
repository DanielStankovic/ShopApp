package com.example.shopapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.shopapp.R
import com.example.shopapp.activity.ProductActivity
import com.example.shopapp.adapter.StoreAdapter
import com.example.shopapp.databinding.FragmentShoppingBinding
import com.example.shopapp.model.Store
import com.example.shopapp.util.RoleHolder
import com.example.shopapp.viewmodel.StoreViewModel
import com.example.shopapp.viewmodel.ViewModelFactory


class ShoppingFragment : Fragment(R.layout.fragment_shopping), StoreAdapter.OnItemClickListener {

    /*Ovo je isto binding kao i u aktivnostima, ovim objektom pristupamo viewovima u layout fajlu
    * U fragmentu se binding dobija na nesto drugaciji nacin nego kod aktivnosti.
    * Kao prvo imamo 2 objekta: _binding i binding. Razlgo je taj sto binding objektu mozemo da pristupimo
    * samo dok je Fragment izmedju onCreateView i onDestroyView state-ova. Na ovaj nacin, obezbedjujemo
    * se da necemo da mu pristupimo ako je slucajno null*/
    private var _binding : FragmentShoppingBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel:StoreViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /*Ovako dobijamo _binding a obican binding koji inace i koristimo dobijamo preko gettera iznad*/
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Ovo je objekat koji predstavlja aplikaciju. Potreban nam je kako bismo instancirali bazu
        val application = requireActivity().application

        /*ViewModelFactory klase sluze kako bi se uz njiihovu pomoc kreirali viewModeli. Zapravo je nesto kao
         konstruktor za ViewModel. Posto nam u ViewModelu treba referenca ka bazi kako bismo mogli da dobijemo Usera ili da insertujemo novog usera
        prosledjujemo application objekat. Application objekat mozemo da iskoristimo da dobijemo bazu unutar viewModela.
        * */
        val viewModelFactory = ViewModelFactory(application)

        /*Ovo je objekat koji koristimo za komunikaciju sa ViewModelom. ViewModel sluzi kako bismo komunicirali sa
       bazom i dobili uvek sveze podatke koje treba da prikazemo na ekranu
       Instancira se dole unutar onCreate metode
        */
        viewModel = ViewModelProvider(this, viewModelFactory).get(StoreViewModel::class.java)

        val adapter = StoreAdapter(this)
        binding.storeRv.adapter = adapter
        binding.storeRv.setHasFixedSize(true)

        viewModel.storeList.observe(viewLifecycleOwner){
            it.let {
                adapter.submitList(it)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        /*Moramo da pocistimo binding objekat iz fragmenta kako ne bi doslo do memory leak-a*/
        _binding = null
    }

    /*Ova metoda je callback iz interfejsa koji je definisan u adapteru. Ova metoda se porkece kada god
    kliknemo na neku prodavnicu. Kada se to desi, kao parametar dobijamo objekat prodacnice koja je kliknuta
    Kao parametre u intent prosledjujemo ime radnje i njen ID. Ime radnje nam treba kako bismo ga postavili
    na vrh forme, a ID radnje nam treba kako bismo mogli da izlistamo artikle koji pripadaju toj radnji*/
    override fun onItemClick(store: Store) {
        //Ovde pokrecemo aktivnost sa artiklima kao i na prethodnim mestima u loginActivity na primer
        val intent = Intent(requireContext(), ProductActivity::class.java)
        intent.putExtra("store_id", store.id)
        intent.putExtra("store_name", store.name)
        startActivity(intent)
    }
}