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
            // Get the title from the EditText
            val title = binding.etTitle.text.toString()

            // show loading progress
            binding.loading.visibility = View.VISIBLE

            // Create a new Category object
            val category = Category(title = title, price = 0, imageURL = selectedImageUri.toString(), isNew = false, subCategories = mapOf())

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

    // Function to handle image picker click
    private fun setupImagePicker() {
        // Registering activity result for image selection
        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                // Handling the selected image URI
                handleSelectedImage(uri)
            })

        // Setting up click listener for the image picker button
        binding.imagePicker.setOnClickListener {
            // Launching the image picker
            galleryImage.launch("image/*")
        }
    }

    // Function to handle the selected image URI
    private fun handleSelectedImage(uri: Uri?) {
        if (uri != null) {
            // Saving the selected image URI
            selectedImageUri = uri

            // Setting the selected image URI to the ImageView
            binding.imagePicker.setImageURI(uri)

        }
    }

}
