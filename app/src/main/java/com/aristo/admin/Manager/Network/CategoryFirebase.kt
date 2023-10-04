package com.aristo.admin.Manager.Network

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.aristo.admin.Datas.CategoryDataHolder
import com.aristo.admin.Datas.DataListHolder
import com.aristo.admin.Manager.SharedPreferencesManager
import com.aristo.admin.model.Category
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.net.URI

class CategoryFirebase {

    companion object{

        val database = FirebaseDatabase.getInstance()
        val categoriesRef: DatabaseReference = database.getReference("Products")
        val storageRef = FirebaseStorage.getInstance().reference

        fun uploadDataToFirebase(context: Context, category: Category, completionHandler: (Boolean, String?) -> Unit) {
            SharedPreferencesManager.initialize(context)

            val categoryId = categoriesRef.push().key
            val updatedCategoryList = ArrayList(CategoryDataHolder.getInstance().getUpdatedCategoryList())
            updatedCategoryList.add(category)

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
                                CategoryDataHolder.getInstance().setUpdatedCategoryList(updatedCategoryList)

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


        fun updateDataToFirebase(activity: Activity, category : Category, completionHandler: (Boolean, String?) -> Unit){

            var subCategoryId : String?

            SharedPreferencesManager.initialize(activity)

            var mainIndex = SharedPreferencesManager.getMainIndex()

            // Set the updated data in DataHolder
            val originalList = CategoryDataHolder.getInstance().getUpdatedCategoryList()

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
            var childList = CategoryDataHolder.getInstance().getChildCategoryList()

            //User selected from sub category index
            if (subIndexList.isNotEmpty()){

                subCategoryId = categoriesRef.push().key

                DataListHolder.getInstance().setSubIDList(subCategoryId!!)

                category.id = subCategoryId

                for(index in 0..<subIndexList.size){

                    referencePath += "subCategories/${DataListHolder.getInstance().getSubIDList()[index + 1]}/"

                }

                if (childList.isNotEmpty()){
                    for (index in 0..<childList.size){

                        addSubcategories(childList[index],0,1, category)
                    }
                }

            }

            // First level of sub categories
            else{

                addSubcategories(originalList[mainIndex],0,1, category)

            }

            var referenceString = baseURL.child(referencePath).toString()

            var restoredReference = Firebase.database.getReferenceFromUrl(referenceString)

            // Upload image to Firebase Storage
            uploadImageToFirebase(category.imageURL.toUri()) { isSuccess, imageUrl ->
                if (isSuccess) {
                    // Image uploaded successfully, set the imageUrl in the category object
                    if (imageUrl != null) {
                        category.imageURL = imageUrl
                    }

                    // Store category data in Firebase Realtime Database
                    restoredReference.setValue(category)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Add Successfully", "Data added successfully")

                                if (DataListHolder.getInstance().getSubIDList().size > 1){
                                    DataListHolder.getInstance().getSubIDList().removeLast()
                                }
                                CategoryDataHolder.getInstance().setChildCategory(category)

                                completionHandler(true, null)

                            } else {
                                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                                completionHandler(false, errorMessage)
                            }
                        }
                } else {
                    Toast.makeText(activity,"Please select one image.",Toast.LENGTH_LONG).show()
                    completionHandler(false, imageUrl) // Failure, pass false and the error message
                }
            }

        }

        fun addSubcategories(category: Category, depth: Int, maxDepth: Int,newCategory: Category) {

            //Log.d("updated Category:" , "updated Category 1: ${category} ---- ${CategoryDataHolder.getInstance().getChildCategoryList()}")

            if (depth >= maxDepth) {
                return
            }

            category.subCategories.add(newCategory)

            Log.d("updated Category:" , "updated Category: $category")

            addSubcategories(newCategory, depth + 1, maxDepth, newCategory)
        }


        fun uploadImageToFirebase(imageUri: Uri, completionHandler: (Boolean, String?) -> Unit) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
            val uploadTask = imageRef.putFile(imageUri)

            uploadTask.addOnSuccessListener { _ ->
                // Image uploaded successfully, get the download URL
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    completionHandler(true, downloadUri.toString()) // Success, pass true and download URL
                }
            }.addOnFailureListener { exception ->
                val errorMessage = exception.message ?: "Failed to upload image"
                completionHandler(false, errorMessage) // Failure, pass false and the error message
            }
        }

    }
}