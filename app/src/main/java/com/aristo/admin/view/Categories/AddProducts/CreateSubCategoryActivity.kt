package com.aristo.admin.view.Categories.AddProducts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.databinding.ActivityCreateSubCategoryBinding
import com.aristo.admin.model.Category
import com.aristo.admin.view.adapters.SubCategoryListRecyclerViewAdapter

class CreateSubCategoryActivity : AppCompatActivity(){

    private lateinit var binding : ActivityCreateSubCategoryBinding
    private val subCategoryAdapter by lazy { SubCategoryListRecyclerViewAdapter(this, ArrayList()) }
    private val subCatLayoutManager by lazy { LinearLayoutManager(this) }
    var mainIndex : Int? = null
    var subCats : ArrayList<Category>? = ArrayList<Category>()

    //private lateinit var subCategory : Category
    //var isFromMain : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateSubCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        SharedPreferencesManager.initialize(this)

        setRecyclerViewAdapter()

        subCats = intent.getSerializableExtra("subCats") as? ArrayList<Category>

        mainIndex = SharedPreferencesManager.getMainIndex()

        binding.createSubCategory.setOnClickListener {

            val intent = Intent(this, AddSubCategoryDetailActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onResume() {
        super.onResume()

        val updatedCategoryList = CategoryDataHolder.getInstance().getUpdatedCategoryList()

        if (SharedPreferencesManager.getIsSaveSubCategory() == true) {
            if (DataListHolder.getInstance().getSubIndexList().isNotEmpty()){

                    Log.d("On Resume", "onResume: in if condition $mainIndex ${DataListHolder.getInstance().getSubIndexList().size} ${updatedCategoryList[mainIndex!!].subCategories}")

                    processSubCategories(updatedCategoryList[mainIndex!!].subCategories)
            }

            else{

                    Log.d("On Resume", "onResume: in else condition $mainIndex ${DataListHolder.getInstance().getSubIndexList().size} ${updatedCategoryList[mainIndex!!].subCategories}")

                    subCategoryAdapter.updateData(updatedCategoryList[mainIndex!!].subCategories)

            }

            SharedPreferencesManager.isSaveSubCategory(false)
        }
        else{

            if (subCats != null){
                if (subCats!!.isNotEmpty()){
                    subCats?.let { subCategoryAdapter.updateData(it) }
                }
            }
            else{
                subCategoryAdapter.updateData(updatedCategoryList[mainIndex!!].subCategories)
            }
        }

    }

    fun processSubCategories(subCategories: List<Category>) {

        for (subCategory in subCategories){
            if (subCategory.subCategories.isNotEmpty()) {

                Log.d("processSubCategories", "processSubCategories: Subcategories found. Processing subcategories... $subCategory")
                processSubCategories(subCategory.subCategories)
            } else {
                Log.d("processSubCategories", "processSubCategories: No subcategories found for this category. $subCategory")

                    subCategoryAdapter.updateData(arrayListOf(subCategory))
                //}

            }

        }


    }

    private fun setRecyclerViewAdapter() {
        // Set up the RecyclerView
        binding.rvSubCategory.layoutManager = subCatLayoutManager
        binding.rvSubCategory.adapter = subCategoryAdapter
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

        if (DataListHolder.getInstance().getSubIndexList().isNotEmpty()){

            DataListHolder.getInstance().getSubIndexList().removeLast()
        }
        if (DataListHolder.getInstance().getSubIDList().isNotEmpty()){

            DataListHolder.getInstance().getSubIDList().removeLast()
        }

    }

}