package com.aristo.admin.view.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.databinding.CategoryListItemsBinding
import com.aristo.admin.databinding.MainCategoryListItemsBinding
import com.aristo.admin.model.Category
import com.aristo.admin.view.Categories.AddProducts.BottomSheet
import com.aristo.admin.view.Categories.AddProducts.CreateMainCategoryActivity
import com.aristo.admin.view.Categories.AddProducts.CreateSubCategoryActivity
import com.aristo.admin.view.Categories.AddProducts.DeleteMainCategoryDialogFragment
import com.aristo.admin.view.Categories.AddProducts.DeleteMainCategoryListener
import com.bumptech.glide.Glide
import com.bumptech.glide.R

class MainCategoryListRecyclerViewAdapter(
    private val showDeleteBottomSheet: BottomSheet,
    val context: Context,
    private val categoryList: ArrayList<Category>
) : RecyclerView.Adapter<MainCategoryListRecyclerViewAdapter.MainCategoryListRecyclerViewHolder>() {

    class MainCategoryListRecyclerViewHolder(private val binding: MainCategoryListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var btnMore = binding.ivMore

        fun bind(category: Category, context: Context, position: Int) {
            binding.tvCatTitle.text = category.title

            Glide.with(context)
                .load(category.imageURL)
                .placeholder(com.aristo.admin.R.drawable.ic_placeholder)
                .into(binding.ivCategory)

            // Set click listeners or perform actions here if needed
            itemView.setOnClickListener {
                // Handle item click
                SharedPreferencesManager.saveCategoryId(category.id)
                val intent = Intent(context, CreateSubCategoryActivity::class.java)

                SharedPreferencesManager.initialize(context)
                SharedPreferencesManager.saveMainIndex(position)
                //SharedPreferencesManager.saveDatabaseChildPath("${category.id}/")
                //DataListHolder.getInstance().setChildPath("${category.id}/")

                context.startActivity(intent)

            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainCategoryListRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainCategoryListItemsBinding.inflate(inflater, parent, false)
        return MainCategoryListRecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MainCategoryListRecyclerViewHolder, position: Int) {

        holder.bind(categoryList[position], context,position)

        holder.btnMore.setOnClickListener {
            showDeleteBottomSheet.onShowBottomSheet(categoryList[position].id, categoryList[position].title)
        }

    }

    fun updateData(newData: List<Category>) {
        categoryList.clear()
        categoryList.addAll(newData)
        notifyDataSetChanged()
    }
}
