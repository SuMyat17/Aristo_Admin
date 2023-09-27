package com.aristo.admin.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aristo.admin.R
import com.aristo.admin.model.Category
import com.aristo.admin.view.ChildCategoriesActivity
import com.aristo.admin.view.ProductDetailActivity

class ChildCategoryListAdapter(private val context: Context, private val childCategoryList: ArrayList<Category>) : RecyclerView.Adapter<ChildCategoryListAdapter.ChildCategoriesListRecyclerViewHolder>() {

    class ChildCategoriesListRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val subCatTitle: TextView = itemView.findViewById(R.id.tvCatTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildCategoriesListRecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_sub_category, parent, false)
        return ChildCategoriesListRecyclerViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return childCategoryList.size
    }

    override fun onBindViewHolder(holder: ChildCategoriesListRecyclerViewHolder, position: Int) {

        holder.subCatTitle.text = childCategoryList[position].title

        holder.itemView.setOnClickListener {

            Toast.makeText(context,"Is Empty ${childCategoryList[position].subCategories.isEmpty()}", Toast.LENGTH_LONG).show()

//            Log.d("aaaaaa", "$childCategoriesList")

            if (childCategoryList[position].subCategories.isNotEmpty()){
                val intent = Intent(context, ChildCategoriesActivity:: class.java)
                intent.putExtra("childCategoriesList", childCategoryList[position].subCategories)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, ProductDetailActivity:: class.java)
                intent.putExtra("product", childCategoryList[position])
                context.startActivity(intent)
                }
            }

//            if (childCategoryList[position].childCategories.isEmpty()){
//                val intent = Intent(context, ProductListActivity:: class.java)
//                intent.putExtra("productList", childCategoryList[position].productList)
//                context.startActivity(intent)
//            }
//            else{
//                val intent = Intent(context, ChildCategoriesActivity:: class.java)
//                intent.putExtra("childCategoriesList", childCategoryList[position].childCategories)
//                context.startActivity(intent)
//            }

    }

}