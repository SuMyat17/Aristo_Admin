package com.aristo.admin.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.databinding.CategoryListItemsBinding
import com.aristo.admin.model.Category
import com.aristo.admin.view.Categories.AddProducts.CreateSubCategoryActivity

class MainCategoryListRecyclerViewAdapter(
    val context: Context,
    private val categoryList: ArrayList<Category>
) : RecyclerView.Adapter<MainCategoryListRecyclerViewAdapter.MainCategoryListRecyclerViewHolder>() {

    class MainCategoryListRecyclerViewHolder(private val binding: CategoryListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category, context: Context, position: Int) {
            binding.tvCatTitle.text = category.title

            // Set click listeners or perform actions here if needed
            itemView.setOnClickListener {
                // Handle item click
                SharedPreferencesManager.saveCategoryId(category.id)
                val intent = Intent(context, CreateSubCategoryActivity::class.java)

                SharedPreferencesManager.initialize(context)
                SharedPreferencesManager.saveMainIndex(position)
                context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainCategoryListRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryListItemsBinding.inflate(inflater, parent, false)
        return MainCategoryListRecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MainCategoryListRecyclerViewHolder, position: Int) {

        holder.bind(categoryList[position], context,position)
    }

    fun updateData(newData: List<Category>) {
        categoryList.clear()
        categoryList.addAll(newData)
        notifyDataSetChanged()
    }
}
