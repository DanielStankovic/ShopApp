package com.example.shopapp.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.databinding.ItemProductLayoutBinding
import com.example.shopapp.model.Product
import com.example.shopapp.util.getCategoryColorByName
import java.io.File
import java.text.DecimalFormat

class ProductAdapter (private val listener:OnItemClickListener) :
ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallback()){

    private val dec = DecimalFormat("#,###.00")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ProductViewHolder {
        val binding = ItemProductLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ProductViewHolder(private val binding:ItemProductLayoutBinding):
            RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
               productCardView.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onItemClick(item, binding.productIv, binding.productNameTv, binding.productDescTv, binding.productPriceTv)
                    }
                }

            }
        }

        fun bind(product: Product) {
            binding.apply {
                productNameTv.text = product.name
                productDescTv.text = product.description
                productPriceTv.text = "RSD ${dec.format(product.price.toDouble())}"
                if(product.isAddedToCart) addedToCartIv.visibility = View.VISIBLE else View.GONE
                if(product.imageName.startsWith("artikal")) {
                    val imageResourceID = root.context.resources.getIdentifier(product.imageName, "drawable", root.context.packageName)
                    Glide.with(root).load(imageResourceID).into(productIv)
                }else{
                    val imagesDirectory = File(root.context.filesDir, "Images")
                    val imageFile = File(imagesDirectory, product.imageName)
                    Glide.with(root).load(imageFile).into(productIv)
                }
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(product:Product,
                        productImage:ImageView,
                        productName:TextView,
                        productDesc:TextView,
                        productPrice:TextView
        )
    }

    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product) =
            oldItem == newItem
    }



}