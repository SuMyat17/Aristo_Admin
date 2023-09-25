package com.smtz.aristo.admin.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smtz.aristo.admin.view.ChildCategoriesActivity
import com.smtz.aristo.admin.R
import com.smtz.aristo.admin.model.Category

class SubCategoryListAdapter(private val context: Context, private val subCategoryList: List<Category>) : RecyclerView.Adapter<SubCategoryListAdapter.SubCategoryListViewHolder>() {

    class SubCategoryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val subCatTitle: TextView = itemView.findViewById(R.id.tvCatTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.view_holder_sub_category, parent, false)
        return SubCategoryListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return subCategoryList.size
    }

    override fun onBindViewHolder(holder: SubCategoryListViewHolder, position: Int) {

        holder.subCatTitle.text = subCategoryList[position].title

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChildCategoriesActivity:: class.java)

            // Put the ArrayList as an extra in the intent
            intent.putExtra("childCategoriesList", subCategoryList[position].subCategories)

            context.startActivity(intent)
        }

    }

}