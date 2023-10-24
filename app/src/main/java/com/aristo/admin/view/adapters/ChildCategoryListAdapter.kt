package com.aristo.admin.view.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.R
import com.aristo.admin.databinding.ViewHolderChildCategoryBinding
import com.aristo.admin.view.ChildCategoriesActivity
import com.aristo.admin.model.Category
import com.aristo.admin.model.NewCategory
import com.aristo.admin.processColorCode
import com.aristo.admin.view.ProductDetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

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
                binding.progressBar.visibility = View.GONE

                Log.d("Color bind", "Color bind: ${processColorCode(category.colorCode)}")
                binding.viewColor.foreground = ColorDrawable(Color.parseColor(processColorCode(category.colorCode)))
            }
            else{
                binding.viewColor.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE

                binding.progressBar.visibility = View.VISIBLE
                Glide.with(context).load(category.imageURL).apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            binding.progressBar.visibility = View.GONE
                            binding.imageView.setImageResource(R.drawable.ic_placeholder)
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            binding.progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(binding.imageView)
            }


            CategoryFirebase.getNewProducts { success, data ->
                if (success) {
                    if (data != null) {
                        for (it in data) {
                            if (category.id == it.id) {
                                binding.ivNew.visibility = View.VISIBLE
                                break
                            } else {
                                binding.ivNew.visibility = View.GONE
                            }

                        }
                    }
                }
            }

////            if (category.subCategories.isEmpty()) {
//                if (category.new) {
//                    binding.ivNew.visibility = View.VISIBLE
//                } else {
//                    binding.ivNew.visibility = View.GONE
//                }
////            } else {
////                binding.ivNew.visibility = View.GONE
////            }

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