package com.aristo.admin.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aristo.admin.R

class SubCategoryListRecyclerViewAdapter (val context: Context) : RecyclerView.Adapter<SubCategoryListRecyclerViewAdapter.SubCategoryListRecyclerViewHolder>(){

    class SubCategoryListRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //val subCatTitle = itemView.findViewById<TextView>(R.id.tvCatTitle)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubCategoryListRecyclerViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.category_list_items, parent, false)
        return SubCategoryListRecyclerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: SubCategoryListRecyclerViewHolder, position: Int) {

        //holder.subCatTitle.setText(childCategoryList[position].title)

        holder.itemView.setOnClickListener {

        }

    }

}