package com.aristo.admin.view.Categories.AddProducts

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.databinding.ActivityAddSubCategoryDetailBinding
import com.aristo.admin.model.Category

class AddSubCategoryDetailActivity : AppCompatActivity(){

    private lateinit var binding : ActivityAddSubCategoryDetailBinding
    lateinit var category : Category
    lateinit var title : String
    var price : Int = 0
    var selectedImageUri: Uri? = null
    var isNew = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddSubCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPreferencesManager.initialize(this)

        setup()

        binding.radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton: RadioButton = findViewById(checkedId)

                when (selectedRadioButton.text) {
                    "ပြမည်" -> {
                        binding.priceLinear.visibility = View.VISIBLE

                    }
                    "မပြပါ" -> {
                        binding.priceLinear.visibility = View.GONE
                    }
                }
            })

        binding.isNewCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            isNew = isChecked
        }

    }

    fun setup(){
        binding.btnCreate.setOnClickListener {

            binding.loading.visibility = View.VISIBLE

            title = binding.etTitle.text.toString()

            if (binding.etPrice.text.toString().isNotEmpty()) {
                price = binding.etPrice.text.toString().toInt()
            }

            category = Category(title = title, price = price, imageURL = selectedImageUri.toString(), isNew = isNew, subCategories = mapOf())

            Log.d("Previous Data", "Previous Data Sub: ${DataListHolder.getInstance().getSubIndexList()}")

            CategoryFirebase.updateDataToFirebase(this@AddSubCategoryDetailActivity,category){ isSuccess, errorMessage ->

                if (isSuccess) {
                    Toast.makeText(this, "Data added successfully", Toast.LENGTH_LONG).show()
                    binding.loading.visibility = View.GONE
                    //CategoryDataHolder.getInstance().setChildCategory(data as Category)
                    finish()
                } else {

                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    binding.loading.visibility = View.GONE

                }

            }

        }

        val galleryImage = registerForActivityResult(
            ActivityResultContracts. GetContent(),
            ActivityResultCallback { uri ->

                if (uri != null) {
                    selectedImageUri = uri
                    binding.imagePicker.setImageURI(uri)
                }
            })

        binding.imagePicker.setOnClickListener {
            galleryImage.launch("image/*")
        }



    }


}