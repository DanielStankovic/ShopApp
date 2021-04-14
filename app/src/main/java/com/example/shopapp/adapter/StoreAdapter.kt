package com.example.shopapp.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.databinding.ItemStoreLayoutBinding
import com.example.shopapp.model.Store
import com.example.shopapp.util.getCategoryColorByName

class StoreAdapter(private val listener: OnItemClickListener) :
        ListAdapter<Store, StoreAdapter.StoreViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = ItemStoreLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding, parent.context.resources, parent.context.packageName)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class StoreViewHolder(private val binding: ItemStoreLayoutBinding, private val resources: Resources, private val packageName:String) :
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

        fun bind(store: Store) {
            binding.apply {
                storeNameTv.text = store.name
                storeDescTv.text = store.description
                storeCategoryTv.text = store.category
                //Ovde menjamo boju na categoriji
                val colorID = getCategoryColorByName(store.category)
                storeCategoryTv.setBackgroundColor(ContextCompat.getColor(root.context, colorID))
                val imageResourceID = resources.getIdentifier(store.imageName , "drawable", packageName)
                Glide.with(root).load(imageResourceID).into(storeImageView)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(store: Store)
    }

    class DiffCallback : DiffUtil.ItemCallback<Store>() {
        override fun areItemsTheSame(oldItem: Store, newItem: Store) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Store, newItem: Store) =
                oldItem == newItem
    }

}