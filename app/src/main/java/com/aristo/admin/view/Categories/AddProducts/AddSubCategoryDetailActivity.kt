package com.aristo.admin.view.Categories.AddProducts

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
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.R
import com.aristo.admin.databinding.ActivityAddSubCategoryDetailBinding
import com.aristo.admin.model.Category

class AddSubCategoryDetailActivity : AppCompatActivity(){

    private lateinit var binding : ActivityAddSubCategoryDetailBinding
    lateinit var category : Category
    lateinit var title : String
    var price : Int = 0
    var selectedImageUri: Uri? = null
    var isNew = false
    var colorCode = ""
    var type = ""
    var isWithImage = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddSubCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPreferencesManager.initialize(this)


        setup()

        // Show Price or not
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


        // Color Code or Image
        binding.showColorRadioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton: RadioButton = findViewById(checkedId)

                when (selectedRadioButton.text) {
                    "ပုံထည့်မည်" -> {
                        binding.imageLayout.visibility = View.VISIBLE
                        binding.colorLayout.visibility = View.GONE
                        isWithImage = true
                    }
                    "ကာလာထည့်မည်" -> {
                        binding.colorLayout.visibility = View.VISIBLE
                        binding.imageLayout.visibility = View.GONE
                        binding.imagePicker.setImageResource(R.drawable.ic_placeholder)
                        selectedImageUri = Uri.parse("")
                        isWithImage = false
                    }
                }
            })

        // isNew or not
        binding.isNewCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            isNew = isChecked
        }

    }

    fun setup(){

        // Click btnCreate button
        binding.btnCreate.setOnClickListener {

            binding.loading.visibility = View.VISIBLE

            title = binding.etTitle.text.toString()

            if (binding.etPrice.text.toString().isNotEmpty()) {
                price = binding.etPrice.text.toString().toInt()
            }

            if (binding.etColorCode.text.toString().isNotEmpty()) {
                colorCode = binding.etColorCode.text.toString()
            }

            if (binding.etType.text.toString().isNotEmpty()) {
                type = binding.etType.text.toString()
            }

            checkToUpload()

        }

        binding.ibBack.setOnClickListener {
            finish()
        }

        // Set selected image url
        val galleryImage = registerForActivityResult(
            ActivityResultContracts. GetContent(),
            ActivityResultCallback { uri ->

                if (uri != null) {
                    selectedImageUri = uri
                    binding.imagePicker.setImageURI(uri)
                }
            })

        // When select image from gallery
        binding.imagePicker.setOnClickListener {
            galleryImage.launch("image/*")
        }

    }

    // Upload Data to Firebase
    fun uploadData(){

        category = Category(title = title, price = price, imageURL = selectedImageUri.toString(), new = isNew, colorCode = colorCode, type = type,subCategories = mapOf())

        CategoryFirebase.updateDataToFirebase(this@AddSubCategoryDetailActivity,category, isWithImage){ isSuccess, errorMessage ->

            if (isSuccess) {
                Toast.makeText(this, "Data added successfully", Toast.LENGTH_LONG).show()
                binding.loading.visibility = View.GONE
                finish()
            } else {

                binding.loading.visibility = View.GONE

            }

        }
    }

    fun checkToUpload(){

        // When user select color code
        if (!isWithImage){

            // Check edit text are empty or not
            if (binding.etType.text.isNotEmpty() &&
                binding.etTitle.text.isNotEmpty() &&
                binding.etColorCode.text.isNotEmpty()){

                // Upload data to firebase
                uploadData()

            }
            else{
                showToast("ကာလာနံပါတ်၊ အမျိုးအစားအမည်၊ အထည် တို့ကိုပြည့်စုံအောင်ဖြည့်ပေးပါ။")
            }
        }
        else{
            // Check edit text are empty or not
            if (binding.etType.text.isNotEmpty() &&
                binding.etTitle.text.isNotEmpty() && selectedImageUri != null) {

                colorCode = ""

                // Upload data to firebase
                uploadData()
            }
            else{
                showToast("ပစ္စည်ှးဓာတ်ပုံ၊ အမျိုးအစားအမည်၊ အထည် တို့ကိုပြည့်စုံအောင်ဖြည့်ပေးပါ။")
            }
        }

    }

    fun showToast(title : String){
        binding.loading.visibility = View.GONE
        Toast.makeText(this, title, Toast.LENGTH_LONG).show()
    }



}