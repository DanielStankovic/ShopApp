package com.example.shopapp.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.databinding.ItemCartLayoutBinding
import com.example.shopapp.databinding.ItemProductLayoutBinding
import com.example.shopapp.model.CartPreview
import com.example.shopapp.model.Product
import java.io.File
import java.text.DecimalFormat

class CartAdapter (private val listener:OnItemClickListener) :
ListAdapter<CartPreview, CartAdapter.CartPreviewViewHolder>(DiffCallback()){

    private val dec = DecimalFormat("#,###.00")

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): CartAdapter.CartPreviewViewHolder {
        val binding = ItemCartLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartPreviewViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CartAdapter.CartPreviewViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class CartPreviewViewHolder(private val binding: ItemCartLayoutBinding):
            RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                cartDeleteProductIv.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onItemDeleteClick(item)
                    }
                }

            }
        }

        fun bind(cartPreview: CartPreview) {
            binding.apply {
                cartProductNameTv.text = cartPreview.productName
                cartProductPriceTv.text = "${dec.format(cartPreview.productPrice.toDouble())} RSD"
                cartTotalPriceTv.text = "${dec.format(cartPreview.productPrice.toDouble() * cartPreview.quantity)} RSD"
                cartProductQtyTv.text = "Količina: ${cartPreview.quantity}"
                if(cartPreview.imageName.startsWith("artikal")) {
                    val imageResourceID = root.context.resources.getIdentifier(cartPreview.imageName, "drawable", root.context.packageName)
                    Glide.with(root).load(imageResourceID).into(cartProductIv)
                }else{
                    val imagesDirectory = File(root.context.filesDir, "Images")
                    val imageFile = File(imagesDirectory, cartPreview.imageName)
                    Glide.with(root).load(imageFile).into(cartProductIv)
                }
            }
        }
    }
    interface OnItemClickListener{
        fun onItemDeleteClick(cartPreview: CartPreview)
    }

    class DiffCallback : DiffUtil.ItemCallback<CartPreview>() {
        override fun areItemsTheSame(oldItem: CartPreview, newItem: CartPreview) =
                oldItem.productID == newItem.productID

        override fun areContentsTheSame(oldItem: CartPreview, newItem: CartPreview) =
                oldItem == newItem
    }
}