package com.aristo.admin.view.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.databinding.CategoryListItemsBinding
import com.aristo.admin.model.Category
import com.aristo.admin.view.Categories.AddProducts.CreateSubCategoryActivity

class SubCategoryListRecyclerViewAdapter (val context: Context,
                                          val categoryList: ArrayList<Category>) : RecyclerView.Adapter<SubCategoryListRecyclerViewAdapter.SubCategoryListRecyclerViewHolder>(){

    class SubCategoryListRecyclerViewHolder(private val binding: CategoryListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category, context: Context, position: Int) {
            binding.tvCatTitle.text = category.title

            // Set click listeners or perform actions here if needed
            itemView.setOnClickListener {
                // Handle item click

                Log.d("Datas ", "Sub Datas : $category")
                val intent = Intent(context, CreateSubCategoryActivity::class.java)

                intent.putExtra("subCats",category.subCategories)

                //SharedPreferencesManager.saveSubCategoryId(category.id)

                //if (!DataListHolder.getInstance().getSubIDList().contains(category.id)){
                    DataListHolder.getInstance().setSubIDList(category.id)
                //}

                if (!CategoryDataHolder.getInstance().getChildCategoryList().contains(category)){
                    CategoryDataHolder.getInstance().setChildCategory(category)
                }
                DataListHolder.getInstance().setSubIndexList(position)

                context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubCategoryListRecyclerViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryListItemsBinding.inflate(inflater, parent, false)
        return SubCategoryListRecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {

        Log.d("updatedsize", "updatedsize: ${categoryList.size}")
        return categoryList.size
    }

    override fun onBindViewHolder(holder: SubCategoryListRecyclerViewHolder, position: Int) {

        holder.bind(categoryList[position], context,position)

    }

    fun updateData(newData: List<Category>) {
        categoryList.clear()
        categoryList.addAll(newData)
        notifyDataSetChanged()
    }

}