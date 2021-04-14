package com.example.shopapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.shopapp.R
import com.example.shopapp.activity.OrderDetailsActivity
import com.example.shopapp.activity.ProductActivity
import com.example.shopapp.adapter.OrderAdapter
import com.example.shopapp.databinding.FragmentOrdersBinding
import com.example.shopapp.databinding.FragmentShoppingBinding
import com.example.shopapp.model.OrderPreview
import com.example.shopapp.viewmodel.OrderViewModel
import com.example.shopapp.viewmodel.StoreViewModel
import com.example.shopapp.viewmodel.ViewModelFactory

class OrdersFragment : Fragment(R.layout.fragment_orders), OrderAdapter.OnItemClickListener {


    /*Ovo je isto binding kao i u aktivnostima, ovim objektom pristupamo viewovima u layout fajlu
    * U fragmentu se binding dobija na nesto drugaciji nacin nego kod aktivnosti.
    * Kao prvo imamo 2 objekta: _binding i binding. Razlgo je taj sto binding objektu mozemo da pristupimo
    * samo dok je Fragment izmedju onCreateView i onDestroyView state-ova. Na ovaj nacin, obezbedjujemo
    * se da necemo da mu pristupimo ako je slucajno null*/
    private var _binding: FragmentOrdersBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: OrderViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /*Ovako dobijamo _binding a obican binding koji inace i koristimo dobijamo preko gettera iznad*/
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrderViewModel::class.java)

        val adapter = OrderAdapter(this)
        binding.ordersRv.setHasFixedSize(true)
        binding.ordersRv.adapter = adapter

        viewModel.orderList.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
                toggleRecyclerViewVisibility(it.isEmpty())
            }
        }

    }

    private fun toggleRecyclerViewVisibility(isOrdersEmpty: Boolean) {
        binding.apply {
            if (isOrdersEmpty) {
                ordersRv.visibility = View.GONE
                noOrdersTv.visibility = View.VISIBLE
            } else {
                ordersRv.visibility = View.VISIBLE
                noOrdersTv.visibility = View.GONE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        /*Moramo da pocistimo binding objekat iz fragmenta kako ne bi doslo do memory leak-a*/
        _binding = null
    }

    override fun onItemClick(orderPreview: OrderPreview) {
        //Ovde pokrecemo aktivnost sa detaljima porudzbine kao i na prethodnim mestima u loginActivity na primer
        val intent = Intent(requireContext(), OrderDetailsActivity::class.java)
        intent.putExtra("order_id", orderPreview.orderID)
        startActivity(intent)

    }

}