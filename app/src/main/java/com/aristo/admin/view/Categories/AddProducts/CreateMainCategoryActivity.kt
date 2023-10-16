package com.aristo.admin.view.Categories.AddProducts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.R
import com.aristo.admin.databinding.ActivityCreateMainCategoryBinding
import com.aristo.admin.databinding.BottomSheetMainCatDeleteBinding
import com.aristo.admin.databinding.BottomSheetMoreBinding
import com.aristo.admin.model.Category
import com.aristo.admin.view.adapters.ChildCategoryListAdapter
import com.aristo.admin.view.adapters.MainCategoryListRecyclerViewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class CreateMainCategoryActivity : AppCompatActivity(), DeleteMainCategoryListener, OnFinishDeleteMain, BottomSheet{

    private lateinit var binding: ActivityCreateMainCategoryBinding
    private val mainCategoryAdapter by lazy { MainCategoryListRecyclerViewAdapter(this,this, ArrayList()) }
    private val mainCatLayoutManager by lazy { LinearLayoutManager(this) }
    private var mainCategoryTitle = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMainCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        DataListHolder.getInstance().deleteAll()
        deleteSharedPreferences()

        binding.createNewCategory.setOnClickListener {
            val intent = Intent(this, AddMainCategoryDetailActivity::class.java)
            startActivity(intent)
        }

        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.mainLoading.visibility = View.VISIBLE

        CategoryFirebase.getMainCategoryData { isSuccess, data ->

            binding.mainLoading.visibility = View.GONE

            if (isSuccess) {
                if (data != null) {

                    mainCategoryAdapter.updateData(data)
                }
            } else {
                Toast.makeText(this, "Can't retrieve datas.", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setupRecyclerView() {
        // Set up the RecyclerView
        binding.rvMainCategory.layoutManager = mainCatLayoutManager
        binding.rvMainCategory.adapter = mainCategoryAdapter
    }

    fun deleteSharedPreferences(){

        SharedPreferencesManager.initialize(this)
        SharedPreferencesManager.removeCategoryId()
        SharedPreferencesManager.removeMainIndex()
    }

    override fun onDeleteMainCategory(mainCatId: String, mainCatTitle : String) {
        val fragmentManager = supportFragmentManager
        val deleteDialog = DeleteMainCategoryDialogFragment()

        mainCategoryTitle = mainCatTitle

        val bundle = Bundle()
        bundle.putString("main_cat_id", mainCatId)
        deleteDialog.arguments = bundle
        deleteDialog.show(fragmentManager, "DeleteMainCategoryDialogFragment")
    }

    override fun onFinishActivity(message: String) {
        Toast.makeText(this, "$mainCategoryTitle has been $message", Toast.LENGTH_LONG).show()
    }

    override fun onShowBottomSheet(mainCatId: String, mainCatTitle : String) {

            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
            val binding = BottomSheetMainCatDeleteBinding.inflate(layoutInflater)
            dialog.setContentView(binding.root)
            dialog.show()

            binding.btnDelete.setOnClickListener {

                (this as? DeleteMainCategoryListener)?.onDeleteMainCategory(mainCatId,mainCatTitle)

                dialog.dismiss()
            }

    }


}
