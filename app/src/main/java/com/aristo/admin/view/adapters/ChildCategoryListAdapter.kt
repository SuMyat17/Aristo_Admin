package com.aristo.admin.view.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.databinding.ViewHolderChildCategoryBinding
import com.aristo.admin.view.ChildCategoriesActivity
import com.aristo.admin.model.Category
import com.aristo.admin.processColorCode
import com.aristo.admin.view.ProductDetailActivity
import com.bumptech.glide.Glide

class ChildCategoryListAdapter(private val context: Context, private val listener: ChildCategoryListener, private val type: String) : RecyclerView.Adapter<ChildCategoryListAdapter.SubCategoryListViewHolder>() {

    private var subCategoryList: List<Category> = listOf()

    class SubCategoryListViewHolder(
        private var binding: ViewHolderChildCategoryBinding,
        private var listener: ChildCategoryListener
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category, context: Context, position: Int, type: String) {
            binding.tvCatTitle.text = category.title

            if (category.colorCode != "" && category.colorCode.count() >6){
                binding.viewColor.visibility = View.VISIBLE
                binding.imageView.visibility = View.GONE
                binding.viewColor.foreground = ColorDrawable(Color.parseColor(processColorCode(category.colorCode)))
            }
            else{
                binding.viewColor.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                Glide.with(context).load(category.imageURL).into(binding.imageView)
            }

            if (category.subCategories.isEmpty()) {
                if (category.new) {
                    binding.ivNew.visibility = View.VISIBLE
                } else {
                    binding.ivNew.visibility = View.GONE
                }
            } else {
                binding.ivNew.visibility = View.GONE
            }

            binding.ivMore.setOnClickListener {
                listener.onTapMore(category, type)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryListViewHolder {
        val itemView = ViewHolderChildCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubCategoryListViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return subCategoryList.size
    }

    override fun onBindViewHolder(holder: SubCategoryListViewHolder, position: Int) {

        holder.bind(subCategoryList[position], context,position,type)

        holder.itemView.setOnClickListener {

            if (subCategoryList[position].subCategories.isNotEmpty()){
                val intent = Intent(context, ChildCategoriesActivity::class.java)
                intent.putExtra("childCategoriesList", subCategoryList[position])
                context.startActivity(intent)
            } else {
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra("product", subCategoryList[position])
                context.startActivity(intent)
            }
        }
    }

    fun setNewData(categories: List<Category>) {
        subCategoryList = categories
        notifyDataSetChanged()
    }

    interface ChildCategoryListener {
        fun onTapMore(category: Category, type: String)
    }

}