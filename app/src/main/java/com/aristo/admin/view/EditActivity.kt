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
import com.aristo.admin.Manager.SharedPreferencesManager.get
import com.aristo.admin.Manager.SharedPreferencesManager.set
import com.aristo.admin.databinding.ActivityEditBinding
import com.aristo.admin.model.Admin
import com.bumptech.glide.Glide

class EditActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditBinding

    private var selectedImageUri: Uri? = null
    private var selectedImageString: String? = null
    private var companyName: String? = null
    private lateinit var admin: Admin

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


        companyName = SharedPreferencesManager.customPrefs(this, "Admin").get("company")


        if (AdminDataHolder.instance.admin == null && companyName != null) {
            CategoryFirebase.getAdmin(companyName!!) { isSuccess, admin ->
                if (isSuccess) {
                    if (admin != null) {
                        this.admin = admin as Admin
                        AdminDataHolder.instance.admin = admin
                        makeCompanyNameUnClickable()
                    }
                }
            }
        } else if (AdminDataHolder.instance.admin != null && companyName != null) {
            admin = AdminDataHolder.instance.admin!!

            binding.etCompanyName.setText(admin.companyName)
            binding.etCompanyAddress.setText(admin.address)
            binding.etCompanyPhone.setText(admin.phone)
            binding.etCompanyViberNo.setText(admin.viber)
            binding.etCompanyFbPage.setText(admin.fbPage)

            if (admin.image != null) {
                selectedImageString = admin.image
                Glide.with(this).load(admin.image).into(binding.ivCompanyLogo)
            }
            makeCompanyNameUnClickable()
        }


        binding.btnSave.setOnClickListener {

            if (binding.etCompanyName.text.toString() != "" && binding.etCompanyAddress.text.toString() != "" && binding.etCompanyPhone.text.toString() != "") {

                binding.progressBar.visibility = View.VISIBLE

                if (companyName == null) {
                    companyName = binding.etCompanyName.text.toString()
                    SharedPreferencesManager.customPrefs(this, "Admin").set("company", companyName)
                }

                if (selectedImageUri != null) {
                    CategoryFirebase.uploadImageToFirebase(selectedImageUri!!) { isSuccessful, imageUrl ->

                        if (isSuccessful) {
                            CategoryFirebase.addAdmin(
                                companyName = companyName!!,
                                address = binding.etCompanyAddress.text.toString(),
                                phone = binding.etCompanyPhone.text.toString(),
                                image = imageUrl,
                                viber = binding.etCompanyViberNo.text.toString(),
                                fbPage = binding.etCompanyFbPage.text.toString(),
                            )
                            { isSuccess, message ->
                                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                                binding.progressBar.visibility = View.GONE
                                finish()
                            }
                        }
                    }
                }
                else if (selectedImageString != null){
                    CategoryFirebase.addAdmin(
                        companyName = companyName!!,
                        address = binding.etCompanyAddress.text.toString(),
                        phone = binding.etCompanyPhone.text.toString(),
                        image = selectedImageString,
                        viber = binding.etCompanyViberNo.text.toString(),
                        fbPage = binding.etCompanyFbPage.text.toString(),
                    )
                    { isSuccess, message ->
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                        finish()
                    }
                }
            }
        }

    }

    private fun makeCompanyNameUnClickable() {
        binding.etCompanyName.isEnabled = false
    }

}