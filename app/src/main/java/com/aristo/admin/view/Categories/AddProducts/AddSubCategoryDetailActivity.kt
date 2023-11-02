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
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.R
import com.aristo.admin.databinding.ActivityAddSubCategoryDetailBinding
import com.aristo.admin.model.Category
import com.aristo.admin.model.NewCategory
import com.bumptech.glide.Glide

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
    var editCategory : Category? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddSubCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedIntent = intent
        if (receivedIntent.hasExtra("Edit")) {
            editCategory = receivedIntent.getSerializableExtra("Edit") as Category
            setUpData()
        }

        if (DataListHolder.getInstance().getIsType().isNotEmpty()){
            if (DataListHolder.getInstance().getIsType().last() == true){
                binding.countingLayout.visibility = View.GONE
                type = CategoryDataHolder.getInstance().countingType

            }
            else{
                binding.countingLayout.visibility = View.VISIBLE
            }
        }
        else{
            binding.countingLayout.visibility = View.VISIBLE
        }

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
                        price = 0
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

            if (binding.etColorCode.text.toString().isNotEmpty() ) {
                colorCode = binding.etColorCode.text.toString()

                if (!colorCode.startsWith("#")){
                    colorCode = "#$colorCode"
                }
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
                    isWithImage = true
                }
            })

        // When select image from gallery
        binding.imagePicker.setOnClickListener {
            galleryImage.launch("image/*")
        }

    }

    // Upload Data to Firebase
    fun uploadData(){

        if (editCategory != null) {
            if (editCategory!!.imageURL.isNotEmpty() && binding.etColorCode.text.toString().isEmpty() && !isWithImage) {
                category = Category(id = editCategory!!.id, title = binding.etTitle.text.toString(), price = price, imageURL = editCategory!!.imageURL, new = isNew, colorCode = "", type = type, subCategories = editCategory!!.subCategories)

                CategoryFirebase.updateCategory(category, CategoryDataHolder.getInstance().updatedCategoryList) { isSuccess, message ->
                    finish()
                }

            } else if (binding.etColorCode.text.toString().isNotEmpty() && !isWithImage) {
                category = Category(id = editCategory!!.id, title = binding.etTitle.text.toString(), price = price, imageURL = "", new = isNew, colorCode = colorCode, type = type, subCategories = editCategory!!.subCategories)

                CategoryFirebase.updateCategory(category, CategoryDataHolder.getInstance().updatedCategoryList) { isSuccess, message ->
                    finish()
                }

            } else if (isWithImage){
                CategoryFirebase.uploadImageToFirebase(imageUri = selectedImageUri!!) { isSuccessImage, imageString ->
                    if (isSuccessImage) {
                        if (imageString != null) {
                            category = Category(id = editCategory!!.id, title = binding.etTitle.text.toString(), price = price, imageURL = imageString, new = isNew, colorCode = "", type = type, subCategories = editCategory!!.subCategories)

                            CategoryFirebase.updateCategory(category, CategoryDataHolder.getInstance().updatedCategoryList) { isSuccess, message ->
                                finish()
                            }
                        }
                    }
                }
            }
            if (isNew) {
                val newProduct = NewCategory(id = category.id)
                CategoryFirebase.addNewProduct(newProduct) { _, _ -> }
            }

            if (!isNew) {
                CategoryFirebase.getNewProducts() { isSuccess, newList ->
                    if (isSuccess) {
                        newList?.forEach {
                            if (it.id == editCategory!!.id) {
                                CategoryFirebase.removeNewProduct(it) { _, _ -> }
                            }
                        }
                    }
                }
            }

        } else {


            category = Category(title = title, price = price, imageURL = selectedImageUri.toString(), new = isNew, colorCode = colorCode, type = type, subCategories = mapOf())

            CategoryFirebase.updateDataToFirebase(this@AddSubCategoryDetailActivity, category, isWithImage) { isSuccess, errorMessage ->

                if (isSuccess) {

                Toast.makeText(this, "Data added successfully", Toast.LENGTH_LONG).show()
                    binding.loading.visibility = View.GONE
                    finish()

                } else {
                    binding.loading.visibility = View.GONE
                }
            }

            if (isNew) {
                val newProduct = NewCategory(id = category.id)
                CategoryFirebase.addNewProduct(newProduct) { _, _ -> }
            }
        }
    }

    fun checkToUpload() {

        var isTypeShow: Boolean

        Log.d("afdasdfasdf", "${isWithImage} ${selectedImageUri}${binding.etTitle.text} ${binding.etColorCode.text} ${binding.etType.text} ${binding.etPrice.text}")

        if (DataListHolder.getInstance().getIsType().isNotEmpty()) {
            if (DataListHolder.getInstance().getIsType().last() == true) {

                // There is counting type
                isTypeShow = false
            } else {

                // There is no counting type
                isTypeShow = true
            }
        } else {

            // There is no counting type
            isTypeShow = true
        }

        // When user select color code
        if (!isWithImage) {

            if (isTypeShow) {
                if (binding.etType.text.isNotEmpty() &&
                    binding.etTitle.text.isNotEmpty() &&
                    binding.etColorCode.text.isNotEmpty()
                ) {

                    Log.d("Color Code", "Color Code: ${binding.etColorCode.text.count()}")

                    if (binding.etColorCode.text.count() in 6..7) {
                        // Upload data to firebase
                        uploadData()
                    } else {
                        showToast("Color code will be 6 characters.")
                    }
                } else if (binding.etTitle.text.isNotEmpty() && editCategory?.imageURL != "") {
                    uploadData()
                } else {
                    showToast("ကာလာနံပါတ်၊ အမျိုးအစားအမည်၊ ရေတွက်ပုံ တို့ကိုပြည့်စုံအောင်ဖြည့်ပေးပါ။")
                }
            } else {
                if (binding.etTitle.text.isNotEmpty() &&
                    binding.etColorCode.text.isNotEmpty()
                ) {

                    Log.d("Color Code", "Color Code: ${binding.etColorCode.text.count()}")

                    if (binding.etColorCode.text.count() in 6..7) {
                        // Upload data to firebase
                        uploadData()
                    } else {
                        showToast("Color code will be 6 characters.")
                    }
                } else if (binding.etTitle.text.isNotEmpty() && editCategory?.imageURL != "") {
                    uploadData()
                } else {
                    showToast("ကာလာနံပါတ်၊ အမျိုးအစားအမည် တို့ကိုပြည့်စုံအောင်ဖြည့်ပေးပါ။")
                }
            }
        } else {
            if (isTypeShow) {
                if (binding.etType.text.isNotEmpty() &&
                    binding.etTitle.text.isNotEmpty()
                ) {
                    if (selectedImageUri != null || editCategory?.imageURL != "") {
                        colorCode = ""
                        // Upload data to firebase
                        uploadData()
                    } else {
                        showToast("ပစ္စည်ှးဓာတ်ပုံံ၊ အမျိုးအစားအမည်၊ ရေတွက်ပုံ တို့ကိုပြည့်စုံအောင်ဖြည့်ပေးပါ။")
                    }
                } else {

                    showToast("ပစ္စည်ှးဓာတ်ပုံံ၊ အမျိုးအစားအမည်၊ ရေတွက်ပုံ တို့ကိုပြည့်စုံအောင်ဖြည့်ပေးပါ။")

                }
            } else {
                if (binding.etTitle.text.isNotEmpty()) {

                    if (selectedImageUri != null || editCategory?.imageURL != "") {

                        colorCode = ""
                        // Upload data to firebase
                        uploadData()
                    } else {
                        showToast("ပစ္စည်ှးဓာတ်ပုံ၊ အမျိုးအစားအမည် တို့ကိုပြည့်စုံအောင်ဖြည့်ပေးပါ။")
                    }
                } else {

                    showToast("ပစ္စည်ှးဓာတ်ပုံ၊ အမျိုးအစားအမည် တို့ကိုပြည့်စုံအောင်ဖြည့်ပေးပါ။")

                }
            }
        }
    }



    fun showToast(title : String){
        binding.loading.visibility = View.GONE
        Toast.makeText(this, title, Toast.LENGTH_LONG).show()
    }

    private fun setUpData() {
        editCategory?.let {
            binding.rbAddPicture.isChecked = true

            if (it.imageURL.isNotEmpty()) {
                binding.rbAddPicture.isChecked = true
                Glide.with(this).load(it.imageURL).into(binding.imagePicker)

                binding.imageLayout.visibility = View.VISIBLE
                binding.colorLayout.visibility = View.GONE
                isWithImage = false

            } else if (it.colorCode.isNotEmpty()) {
                binding.rbAddColorCode.isChecked = true
                colorCode = it.colorCode
                binding.etColorCode.setText(it.colorCode)

                binding.colorLayout.visibility = View.VISIBLE
                binding.imageLayout.visibility = View.GONE
                selectedImageUri = Uri.parse("")
                isWithImage = false
            }

            binding.etTitle.setText(it.title)
            binding.etType.setText(it.type)
            if (it.price != 0) {
                binding.rbShown.isChecked = true
                binding.priceLinear.visibility = View.VISIBLE
                binding.etPrice.setText(it.price.toString())
            } else {
                binding.rbNotShown.isChecked = true
            }

            CategoryFirebase.getNewProducts { isSuccess, data ->
                if (isSuccess) {
                    data?.forEach { newList ->
                        if (it.id == newList.id) {
                            binding.isNewCheckBox.isChecked = true
                            isNew = true
                        }
                    }
                }
            }
        }
    }

}