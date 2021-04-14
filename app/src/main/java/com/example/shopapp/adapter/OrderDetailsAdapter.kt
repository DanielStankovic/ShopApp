package com.example.shopapp.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.databinding.ItemOrderDetailsBinding
import com.example.shopapp.model.OrderDetailsPreview
import java.io.File
import java.text.DecimalFormat

class OrderDetailsAdapter() :
    ListAdapter<OrderDetailsPreview, OrderDetailsAdapter.OrderDetailsPreviewViewHolder>(DiffCallback()) {

    private val dec = DecimalFormat("#,###.00")


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDetailsAdapter.OrderDetailsPreviewViewHolder {
        val binding =
            ItemOrderDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsPreviewViewHolder(binding)
    }


    override fun onBindViewHolder(holder: OrderDetailsPreviewViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class OrderDetailsPreviewViewHolder(private val binding: ItemOrderDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderDetailsPreview: OrderDetailsPreview) {
            binding.apply {
                ordDetProductNameTv.text = orderDetailsPreview.productName
                ordDetProductPriceTv.text =
                    "${dec.format(orderDetailsPreview.productPrice.toDouble())} RSD"
                ordDetTotalPriceTv.text =
                    "${dec.format(orderDetailsPreview.productPrice.toDouble() * orderDetailsPreview.quantity)} RSD"
                ordDetProductQtyTv.text = "Količina: ${orderDetailsPreview.quantity}"

                if(orderDetailsPreview.imageName.startsWith("artikal")) {
                    val imageResourceID = root.context.resources.getIdentifier(orderDetailsPreview.imageName, "drawable", root.context.packageName)
                    Glide.with(root).load(imageResourceID).into(ordDetProductIv)
                }else{
                    val imagesDirectory = File(root.context.filesDir, "Images")
                    val imageFile = File(imagesDirectory, orderDetailsPreview.imageName)
                    Glide.with(root).load(imageFile).into(ordDetProductIv)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<OrderDetailsPreview>() {
        override fun areItemsTheSame(oldItem: OrderDetailsPreview, newItem: OrderDetailsPreview) =
            oldItem.productName == newItem.productName

        override fun areContentsTheSame(
            oldItem: OrderDetailsPreview,
            newItem: OrderDetailsPreview
        ) =
            oldItem == newItem
    }


}