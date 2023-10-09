package com.aristo.admin.view.Categories.AddProducts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.databinding.ActivityCreateSubCategoryBinding
import com.aristo.admin.model.Category
import com.aristo.admin.view.adapters.SubCategoryListRecyclerViewAdapter
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateSubCategoryActivity : AppCompatActivity(){

    private lateinit var binding : ActivityCreateSubCategoryBinding
    private val subCategoryAdapter by lazy { SubCategoryListRecyclerViewAdapter(this, ArrayList()) }
    private val subCatLayoutManager by lazy { LinearLayoutManager(this) }
    var mainIndex : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateSubCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        SharedPreferencesManager.initialize(this)

        setRecyclerViewAdapter()

        mainIndex = SharedPreferencesManager.getMainIndex()

        binding.createSubCategory.setOnClickListener {

            val intent = Intent(this, AddSubCategoryDetailActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onResume() {
        super.onResume()

        binding.subLoading.visibility = View.VISIBLE

        CategoryFirebase.getCategoriesDatas(this) { isSuccess, data ->

            binding.subLoading.visibility = View.GONE

            if (isSuccess) {
                if (data != null) {

                    subCategoryAdapter.updateData(data)
                }
            } else {
                Toast.makeText(this, "Can't retrieve datas.", Toast.LENGTH_LONG).show()
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
        if (DataListHolder.getInstance().getChildPath().isNotEmpty()){

            DataListHolder.getInstance().getChildPath().removeLast()
        }
    }

}