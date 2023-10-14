package com.aristo.admin.Manager.Network

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.aristo.admin.Datas.AdminDataHolder
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.model.Admin
import com.aristo.admin.model.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class CategoryFirebase {

    companion object{

        val database = FirebaseDatabase.getInstance()
        val categoriesRef: DatabaseReference = database.getReference("Products")
        var referencePathUpDateData = "Products/Categories/"
        
        fun uploadDataToFirebase(context: Context, category: Category, completionHandler: (Boolean, String?) -> Unit) {
            SharedPreferencesManager.initialize(context)

            val categoryId = categoriesRef.push().key

            SharedPreferencesManager.saveCategoryId(categoryId!!)

            category.id = categoryId

            // Upload image to Firebase Storage
            uploadImageToFirebase(category.imageURL.toUri()) { isSuccess, imageUrl ->
                if (isSuccess) {
                    // Image uploaded successfully, set the imageUrl in the category object
                    if (imageUrl != null) {
                        category.imageURL = imageUrl
                    }

                    // Store category data in Firebase Realtime Database
                    categoriesRef.child("Categories").child(categoryId).setValue(category)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                completionHandler(true, null) // Success, pass true and no error message

                            } else {
                                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                                completionHandler(false, errorMessage) // Failure, pass false and the error message
                            }
                        }
                } else {
                    Toast.makeText(context,"Please select one image.",Toast.LENGTH_LONG).show()
                    completionHandler(false, imageUrl) // Failure, pass false and the error message
                }
            }


        }


        fun updateDataToFirebase(activity: Activity, category : Category, isWithImage : Boolean, completionHandler: (Boolean, String?) -> Unit){

            var subCategoryId : String?

            SharedPreferencesManager.initialize(activity)

            var categoryId = SharedPreferencesManager.getCategoryId()

            if (DataListHolder.getInstance().getSubIDList().isEmpty()){
                subCategoryId = categoriesRef.push().key
                //DataListHolder.getInstance().setSubIDList(subCategoryId!!)
                if (subCategoryId != null) {
                    category.id = subCategoryId
                }
            }
            else{
                subCategoryId = DataListHolder.getInstance().getSubIDList()[0]
            }

            val subIndexList = DataListHolder.getInstance().getSubIndexList()
            var baseURL = categoriesRef.child("Categories").child(categoryId!!)
            var referencePath = "subCategories/$subCategoryId/"

            //User selected from sub category index
            if (subIndexList.isNotEmpty()){

                subCategoryId = categoriesRef.push().key

                DataListHolder.getInstance().setSubIDList(subCategoryId!!)

                category.id = subCategoryId

                for(index in 0..<subIndexList.size){

                    referencePath += "subCategories/${DataListHolder.getInstance().getSubIDList()[index + 1]}/"

                }
            }

            var referenceString = baseURL.child(referencePath).toString()
            var restoredReference = Firebase.database.getReferenceFromUrl(referenceString)

            // Upload data with image
            if (isWithImage){
                // Upload image to Firebase Storage
                uploadImageToFirebase(category.imageURL.toUri()) { isSuccess, imageUrl ->
                    if (isSuccess) {
                        // Image uploaded successfully, set the imageUrl in the category object
                        if (imageUrl != null) {
                            category.imageURL = imageUrl
                        }

                        postSubCategoryDatas(category, restoredReference){ isSuccess, message ->
                            if (isSuccess) {
                                completionHandler(true,null)
                            } else {
                                completionHandler(false, message)
                            }
                        }

                    } else {
                        Toast.makeText(activity,"Please select one image.",Toast.LENGTH_LONG).show()
                        completionHandler(false, imageUrl) // Failure, pass false and the error message
                    }
                }
            }

            // Upload data with color code
            else{

                postSubCategoryDatas(category, restoredReference){ isSuccess, message ->
                    if (isSuccess) {
                        completionHandler(true,null)
                    } else {
                        completionHandler(false, message)
                    }
                }
            }

        }

        fun postSubCategoryDatas(category: Category, restoredReference : DatabaseReference, completionHandler: (Boolean, String?) -> Unit){
            // Store category data in Firebase Realtime Database
            restoredReference.setValue(category)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Add Successfully", "Data added successfully")

                        if (DataListHolder.getInstance().getSubIDList().size > 1){
                            DataListHolder.getInstance().getSubIDList().removeLast()
                        }
                        completionHandler(true, null)
                    }

                    else {
                        val errorMessage = task.exception?.message ?: "Unknown error occurred"
                        completionHandler(false, errorMessage)
                    }
                }
        }

        fun uploadImageToFirebase(imageUri: Uri, completionHandler: (Boolean, String?) -> Unit) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
            val uploadTask = imageRef.putFile(imageUri)

            uploadTask.addOnSuccessListener { _ ->
                // Image uploaded successfully, get the download URL
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    completionHandler(true, downloadUri.toString()) // Succes, pass true and download URL
                }
            }.addOnFailureListener { exception ->
                val errorMessage = exception.message ?: "Failed to upload image"
                completionHandler(false, errorMessage) // Failure, pass false and the error message
            }
        }
        fun getMainCategoryData(completionHandler: (Boolean, ArrayList<Category>?) -> Unit) {

            categoriesRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val subCategoriesSnapshot = snapshot.child("Categories")
                    val subCategoriesList: ArrayList<Category> = ArrayList()

                    for (categorySnapshot in subCategoriesSnapshot.children) {

                        val subCategory = categorySnapshot.getValue(Category::class.java)
                        subCategory?.let {
                            subCategoriesList.add(it)
                        }

                    }
                    completionHandler(true,subCategoriesList)
                }

                override fun onCancelled(error: DatabaseError) {
                    completionHandler(false,null)
                }

            })
        }

        fun getCategoriesDatas(activity: Activity,completionHandler: (Boolean, ArrayList<Category>?) -> Unit){

            SharedPreferencesManager.initialize(activity)
            var mainCatID = SharedPreferencesManager.getCategoryId()
            var baseURL = mainCatID?.let {
                categoriesRef.child("Categories").child(
                    it
                )
            }
            val subIndexList = DataListHolder.getInstance().getSubIndexList()
            var referencePath = ""

            for(index in 0..<subIndexList.size){

                referencePath += "subCategories/${com.aristo.admin.Datas.DataListHolder.getInstance().getSubIDList()[index]}/"

            }
            var referenceString = baseURL?.child(referencePath).toString()
            var categoryRef = Firebase.database.getReferenceFromUrl(referenceString)

            categoryRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val subCategoriesSnapshot = snapshot.child("subCategories")
                    val subCategoriesList: ArrayList<Category> = ArrayList()

                    for (categorySnapshot in subCategoriesSnapshot.children) {

                        val subCategory = categorySnapshot.getValue(Category::class.java)
                        subCategory?.let {
                            subCategoriesList.add(it)
                        }

                    }

                    completionHandler(true,subCategoriesList)
                }

                override fun onCancelled(error: DatabaseError) {
                    completionHandler(false,null)
                }

            })
        }

        fun addAdmin(admin: Admin, completionHandler: (Boolean, String?) -> Unit) {

            AdminDataHolder.instance.admin = admin

            database.reference.child("companyInfo").setValue(admin).addOnCompleteListener {
                if (it.isSuccessful) {
                    completionHandler(true, "Successful")
                } else {
                    completionHandler(false, it.exception?.message)
                }
            }
        }

        fun getAdmin(completionHandler: (Boolean, Any?) -> Unit) {

            database.reference.child("companyInfo").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val admin = snapshot.getValue(Admin::class.java)
                    AdminDataHolder.instance.admin = admin as Admin
                    completionHandler(true, admin)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("asfdasf", "cancelled")
                    completionHandler(false, error.message)
                }

            })
        }

        fun updateCategory(category: Category, completionHandler: (Boolean, String?) -> Unit) {
            referencePathUpDateData = "Products/Categories/"

            CategoryDataHolder.getInstance().updatedCategoryList.forEach {
                referencePathUpDateData += "${it.id}/subCategories/"
            }
            referencePathUpDateData += category.id

            val referenceString = database.reference.child(referencePathUpDateData).toString()

            val restoredReference = Firebase.database.getReferenceFromUrl(referenceString)
            restoredReference.setValue(category).addOnCompleteListener {
                if (it.isSuccessful) {
                    completionHandler(true, "updated")
                } else {
                    completionHandler(false, it.exception?.message)
                }
            }
        }

        fun deleteCategory(category: Category, completionHandler: (Boolean, String?) -> Unit) {
            referencePathUpDateData = "Products/Categories/"

            CategoryDataHolder.getInstance().updatedCategoryList.forEach {
                referencePathUpDateData += "${it.id}/subCategories/"
            }
            referencePathUpDateData += category.id

            val referenceString = database.reference.child(referencePathUpDateData).toString()

            val restoredReference = Firebase.database.getReferenceFromUrl(referenceString)
            restoredReference.removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    completionHandler(true, "deleted")
                } else {
                    completionHandler(false, it.exception?.message)
                }
            }
        }
    }
}