package com.example.shopapp.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.shopapp.R
import com.example.shopapp.adapter.CartAdapter
import com.example.shopapp.databinding.FragmentCartBinding
import com.example.shopapp.databinding.FragmentShoppingBinding
import com.example.shopapp.model.CartPreview
import com.example.shopapp.util.getPaymentMethodNameByID
import com.example.shopapp.viewmodel.CartViewModel
import com.example.shopapp.viewmodel.StoreViewModel
import com.example.shopapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import java.text.DecimalFormat

class CartFragment : Fragment(R.layout.fragment_cart), CartAdapter.OnItemClickListener {

    /*Ovo je isto binding kao i u aktivnostima, ovim objektom pristupamo viewovima u layout fajlu
   * U fragmentu se binding dobija na nesto drugaciji nacin nego kod aktivnosti.
   * Kao prvo imamo 2 objekta: _binding i binding. Razlgo je taj sto binding objektu mozemo da pristupimo
   * samo dok je Fragment izmedju onCreateView i onDestroyView state-ova. Na ovaj nacin, obezbedjujemo
   * se da necemo da mu pristupimo ako je slucajno null*/
    private var _binding: FragmentCartBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: CartViewModel

    private var paymentTotal = 0
    private var productsInCart = listOf<CartPreview>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /*Ovako dobijamo _binding a obican binding koji inace i koristimo dobijamo preko gettera iznad*/
        _binding = FragmentCartBinding.inflate(inflater, container, false)
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
        viewModel = ViewModelProvider(this, viewModelFactory).get(CartViewModel::class.java)

        val adapter = CartAdapter(this)
        binding.cartRv.adapter = adapter
        binding.cartRv.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.cartEvent.collect { event ->
                when (event) {
                    is CartViewModel.CartEvent.ProductDeleted -> {
                        //Ovaj event znaci da je proizvod dodat uspesno
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()

                    }
                    is CartViewModel.CartEvent.ErrorProductDeleted -> {
                        //Ovaj event znaci da je proizvod dodat uspesno
                        Toast.makeText(requireContext(), event.errorMessage, Toast.LENGTH_LONG).show()

                    }
                    is CartViewModel.CartEvent.OrderCreated -> {
                        //Ovaj event znaci da je porudzbina napravljena uspesno
                        Toast.makeText(requireContext(), event.createdMessage, Toast.LENGTH_LONG).show()
                    }
                    is CartViewModel.CartEvent.ErrorOrderCreated -> {
                        //Ovaj event znaci da je doslo do greske pri porucivanju
                        Toast.makeText(requireContext(), event.errorCreatedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewModel.cartPreviewList.observe(viewLifecycleOwner) {
            it?.let {
                productsInCart = it
                toggleRecyclerViewVisibility(it.isEmpty())
                adapter.submitList(it)
                val totalPrice = it.sumBy { cartPreview -> (cartPreview.quantity * cartPreview.productPrice) }
                if (totalPrice > 0)
                    binding.totalPriceTv.text = "Ukupno: ${DecimalFormat("#,###.00").format(totalPrice)} RSD"
                else
                    binding.totalPriceTv.text = "Ukupno: 0.00 RSD"

                paymentTotal = totalPrice
            }
        }

        //Postavljanje osluskivaca za dugme za porucivanje
        setupListeners()

    }

    private fun setupListeners() {
        binding.orderBtn.setOnClickListener {
            showPaymentMethodDialog()
        }
    }

    private fun toggleRecyclerViewVisibility(isCartEmpty: Boolean) {
        binding.apply {
            if (isCartEmpty) {
                cartRv.visibility = View.GONE
                emptyCartTv.visibility = View.VISIBLE
                orderBtn.isEnabled = false
            } else {
                cartRv.visibility = View.VISIBLE
                emptyCartTv.visibility = View.GONE
                orderBtn.isEnabled = true
            }
        }
    }


    private fun showPaymentMethodDialog() {
        var paymentMethodID = -1;
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_payment_method)
        val creditCardCv = dialog.findViewById(R.id.creditCardCv) as CardView
        val moneyCardCv = dialog.findViewById(R.id.moneyCardCv) as CardView

        creditCardCv.setOnClickListener {
            paymentMethodID = 1
            dialog.dismiss()
            showConfirmationDialog(paymentMethodID)
        }
        moneyCardCv.setOnClickListener {
            paymentMethodID = 2
            dialog.dismiss()
            showConfirmationDialog(paymentMethodID)
        }

        dialog.show()
    }

    private fun showConfirmationDialog(paymentMethodID:Int) {
        //Instanciranje variable bildera koja postavlja sve na dijaliogu
        val builder = AlertDialog.Builder(requireContext())

        // Posavljanje naslova
        builder.setTitle("Da li želite da napravite porudzbinu?")

        //Postavljanje poruke
        builder.setMessage("Ukupno za uplatu: ${DecimalFormat("#,###.00").format(paymentTotal)} RSD\nNačin plaćanja: ${getPaymentMethodNameByID(paymentMethodID)}")

        //Postavljanje dugmeta DA
        builder.setPositiveButton(
                "DA") { dialog, _ ->
            viewModel.orderProducts(paymentMethodID, productsInCart)
        }

        //Postavljanje dugmeta NE
        builder.setNegativeButton(
                "NE") { _, _ ->
           //Ovde se automatski samo gasi dijalog
        }

        builder.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        /*Moramo da pocistimo binding objekat iz fragmenta kako ne bi doslo do memory leak-a*/
        _binding = null
    }

    override fun onItemDeleteClick(cartPreview: CartPreview) {
        viewModel.deleteProductFromCart(cartPreview.userID, cartPreview.productID)
    }

}