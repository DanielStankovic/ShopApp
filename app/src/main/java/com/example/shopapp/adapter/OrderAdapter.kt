package com.example.shopapp.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.databinding.ItemOrderBinding
import com.example.shopapp.model.OrderPreview
import com.example.shopapp.util.convertLongToTime
import com.example.shopapp.util.getPaymentMethodNameByID
import java.text.DecimalFormat


class OrderAdapter(val listener: OnItemClickListener) :
        ListAdapter<OrderPreview, OrderAdapter.OrderViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
            RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onItemClick(item)
                    }
                }

            }
        }

        fun bind(orderPreview: OrderPreview) {
            binding.apply {
                orderIdTv.text = "Šifra: ${orderPreview.orderID}"
                userNameTv.text = "Poručio: ${orderPreview.userName}"
                paymentMethodTv.text = "Plaćanje: ${getPaymentMethodNameByID(orderPreview.methodPayment)}"
                dateCreatedTv.text = "Kreirano: ${convertLongToTime(orderPreview.dateCreated)}"
                totalPriceTv.text = "Ukupno: ${DecimalFormat("#,###.00").format(orderPreview.totalPrice)}"
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(orderPreview: OrderPreview)
    }

    class DiffCallback : DiffUtil.ItemCallback<OrderPreview>() {
        override fun areItemsTheSame(oldItem: OrderPreview, newItem: OrderPreview) =
                oldItem.orderID == newItem.orderID

        override fun areContentsTheSame(oldItem: OrderPreview, newItem: OrderPreview) =
                oldItem == newItem
    }
}