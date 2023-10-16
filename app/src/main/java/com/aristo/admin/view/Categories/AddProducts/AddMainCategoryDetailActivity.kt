package com.aristo.admin.view.Categories.AddProducts

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.databinding.ActivityAddMainCategoryDetailBinding
import com.aristo.admin.model.Category

class AddMainCategoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMainCategoryDetailBinding
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMainCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting up click listener for the create button
        setupButtonClick()

        // Setting up click listener for the image picker
        setupImagePicker()
    }

    // Function to handle create button click
    private fun setupButtonClick() {
        binding.btnCreate.setOnClickListener {

            // show loading progress
            binding.loading.visibility = View.VISIBLE
            checkToUpload()
        }

        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    // Function to handle image picker click
    private fun setupImagePicker() {
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

    fun checkToUpload(){

        // Check edit text are empty or not
        if (binding.etTitle.text.isNotEmpty() &&
            selectedImageUri != null){

            // Upload data to firebase
            uploadData()

        }
        else{
            showToast("အမျိုးအစားအမည်၊ ပစ္စည်ှးဓာတ်ပုံ တို့ကိုပြည့်စုံအောင်ဖြည့်ပေးပါ။")
        }

    }

    fun showToast(title : String){
        binding.loading.visibility = View.GONE
        Toast.makeText(this, title, Toast.LENGTH_LONG).show()
    }

    fun uploadData(){
        // Create a new Category object
        val category = Category(title = binding.etTitle.text.toString(), price = 0, imageURL = selectedImageUri.toString(), new = false, subCategories = mapOf())

        // Upload the category data to Firebase
        CategoryFirebase.uploadDataToFirebase(this, category) { isSuccess, errorMessage ->
            if (isSuccess) {
                Toast.makeText(this, "Data added successfully", Toast.LENGTH_LONG).show()
                binding.loading.visibility = View.GONE

                finish()
            } else {

                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                binding.loading.visibility = View.GONE

            }
        }
    }

}
