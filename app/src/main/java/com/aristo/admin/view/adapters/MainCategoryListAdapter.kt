package com.aristo.admin.view.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aristo.admin.R
import com.aristo.admin.databinding.ViewHolderMainCategoryBinding
import com.aristo.admin.model.Category
import com.aristo.admin.processColorCode
import com.bumptech.glide.Glide

class MainCategoryListAdapter(private val context: Context, private val listener: MainCategoriesRecyclerViewListener) : RecyclerView.Adapter<MainCategoryListAdapter.MainCategoryListViewHolder>() {

    private var selectedPosition = 0
    private var mainCategoryList = listOf<Category>()

    class MainCategoryListViewHolder(private var binding: ViewHolderMainCategoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category, context: Context, position: Int) {
            binding.tvCatTitle.text = category.title

            if (category.colorCode != ""){
                binding.viewColor.visibility = View.VISIBLE
                binding.imageView.visibility = View.GONE

                binding.viewColor.foreground = ColorDrawable(Color.parseColor(processColorCode(category.colorCode)))
            }
            else{
                binding.viewColor.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                Glide.with(context).load(category.imageURL).into(binding.imageView)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryListViewHolder {
        val binding = ViewHolderMainCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainCategoryListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mainCategoryList.size
    }

    override fun onBindViewHolder(holder: MainCategoryListViewHolder, position: Int) {
        holder.bind(mainCategoryList[position], context,position)

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

            listener.reloadSubCategoriesRecyclerView(position)
        }
    }

    fun setNewData(categories: List<Category>) {
        mainCategoryList = categories
        notifyDataSetChanged()
    }

    interface MainCategoriesRecyclerViewListener {
        fun reloadSubCategoriesRecyclerView(index : Int)
    }
}
