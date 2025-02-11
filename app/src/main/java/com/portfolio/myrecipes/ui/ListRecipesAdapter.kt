package com.portfolio.myrecipes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.portfolio.myrecipes.data.Recipes
import com.portfolio.myrecipes.databinding.ItemRowRecipesBinding

class ListRecipesAdapter (private val listRecipes: ArrayList<Recipes>) : RecyclerView.Adapter<ListRecipesAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, time, serving, _, _, photo) = listRecipes[position]
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.binding.imgItemPhoto)
        holder.binding.tvItemName.text = name
        holder.binding.tvItemTime.text = time
        holder.binding.tvItemServing.text = serving
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listRecipes[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listRecipes.size
    }

    class ListViewHolder(var binding: ItemRowRecipesBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: Recipes)
    }
}