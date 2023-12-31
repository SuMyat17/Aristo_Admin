package com.aristo.admin.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.aristo.admin.Datas.AdminDataHolder
import com.aristo.admin.Manager.Network.CategoryFirebase
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.databinding.ActivityEditBinding
import com.aristo.admin.model.Admin
import com.bumptech.glide.Glide

class EditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditBinding

    private var selectedImageUri: Uri? = null
    private var admin: Admin = Admin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibBack.setOnClickListener {
            finish()
        }

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                binding.ivCompanyLogo.setImageURI(uri)
            }
        }

        binding.btnEditPhoto.setOnClickListener {
            galleryImage.launch("image/*")
        }

        if (AdminDataHolder.instance.admin == null) {
            CategoryFirebase.getAdmin { isSuccess, admin ->
                if (isSuccess) {
                    if (admin != null) {
                        this.admin = admin as Admin
                        AdminDataHolder.instance.admin = admin
                        setUpData(admin)
                    }
                }
            }
        } else if (AdminDataHolder.instance.admin != null) {
            admin = AdminDataHolder.instance.admin!!
            setUpData(admin)
        }


        binding.btnSave.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE

            admin.companyName = admin.companyName
            admin.address = binding.etCompanyAddress.text.toString()
            admin.phone = binding.etCompanyPhone.text.toString()
            admin.viber = binding.etCompanyViberNo.text.toString()
            admin.fbPage = binding.etCompanyFbPage.text.toString()
            admin.fbPageLink = binding.etCompanyFbPageLink.text.toString()

            CategoryFirebase.addAdmin(admin) { isSuccess, message ->
                binding.progressBar.visibility = View.GONE
                finish()
            }

//            if (selectedImageUri != null) {
//                    CategoryFirebase.uploadImageToFirebase(selectedImageUri!!) { isSuccessful, imageUrl ->
//
//                        if (isSuccessful) {
//                            admin.image = imageUrl
//                            CategoryFirebase.addAdmin(admin) { isSuccess, message ->
//                                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                                binding.progressBar.visibility = View.GONE
//                                finish()
//                            }
//                        }
//                    }
//                }
//                else if (selectedImageString != null){
//                    admin.image = selectedImageString
//                    CategoryFirebase.addAdmin(admin) { isSuccess, message ->
////                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                        binding.progressBar.visibility = View.GONE
//                        finish()
//                    }
//                } else {
//                    admin.image = null
//                    CategoryFirebase.addAdmin(admin) { isSuccess, message ->
////                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                        binding.progressBar.visibility = View.GONE
//                        finish()
//                    }
//                }
//            }
//        }
        }

    }

    private fun setUpData(admin: Admin) {

        binding.etCompanyAddress.setText(admin.address)
        binding.etCompanyPhone.setText(admin.phone)
        binding.etCompanyViberNo.setText(admin.viber)
        binding.etCompanyFbPage.setText(admin.fbPage)
        binding.etCompanyFbPageLink.setText(admin.fbPageLink)

    }

}