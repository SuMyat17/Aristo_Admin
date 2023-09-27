package com.aristo.admin.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aristo.admin.R
import com.aristo.admin.model.Category

class MainCategoryListAdapter(private val context: MainCategoriesRecyclerViewListener, private val mainCategoryList: ArrayList<Category>) : RecyclerView.Adapter<MainCategoryListAdapter.MainCategoryListViewHolder>() {

    private var selectedPosition = 0

    class MainCategoryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mainCatTitle: TextView = itemView.findViewById(R.id.tvCatTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_main_category, parent, false)
        return MainCategoryListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mainCategoryList.size
    }

    override fun onBindViewHolder(holder: MainCategoryListViewHolder, position: Int) {
        holder.mainCatTitle.text = mainCategoryList[position].title

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.color.white)
        } else {
            holder.itemView.setBackgroundResource(R.color.background)
        }

        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            notifyItemChanged(previousSelectedPosition)

            notifyItemChanged(selectedPosition)

            context.reloadSubCategoriesRecyclerView(holder.adapterPosition)
        }
    }
}

interface MainCategoriesRecyclerViewListener {
    fun reloadSubCategoriesRecyclerView(index : Int)
}