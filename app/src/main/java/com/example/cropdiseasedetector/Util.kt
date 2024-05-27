package com.example.cropdiseasedetector

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

val storage = Firebase.storage
val storageRef = storage.reference
val imageRef = storageRef.child("Crops")
val database = FirebaseDatabase.getInstance("https://crop-disease-detector-f-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()


fun makeToast(context: Context, txt: String, dur: Long = 3500){
    val toast = Toast.makeText(context, txt, Toast.LENGTH_LONG)
    toast.duration = Toast.LENGTH_LONG // set to long duration
    Handler(Looper.getMainLooper()).postDelayed({
        toast.cancel()
    }, dur)
    toast.show()
}

fun uploadImage(file: Uri, context: Context){

//    val riversRef = storageRef.child("images/$")
    val uploadTask = imageRef.putFile(file)

// Register observers to listen for when the download is done or if it fails
    uploadTask.addOnFailureListener {
        makeToast(context,"Image Uploaded Failed!!!")
    }.addOnSuccessListener { taskSnapshot ->
        makeToast(context = context, "Image Upload Successfully")
    }
}

fun updateTest(uri: Uri, context: Context) : String{
//    val contentResolver = context.contentResolver
//    val cursor = contentResolver.query(file, null, null, null, null)
//
//    var fileName: String? = null
//    if (cursor != null && cursor.moveToFirst()) {
//        val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//        fileName = cursor.getString(displayNameIndex).toString()
//        cursor.close()
//    }
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {

            val displayNameIndex = it.getColumnIndex("_display_name")
            if (it.moveToFirst()) {
                result = if (displayNameIndex != -1) it.getString(displayNameIndex) else null
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != null && cut != -1) {
            result = result?.substring(cut + 1)
        }
    }
    if(result == null) result = ""
    val fileName = result
    Log.i("Image Name" , "$fileName")
    var label = "";
    for (letter in fileName!!){
        if(letter in '0'..'9'){
            break
        }
        label += letter
    }
    Log.i("Image Name" , "$label")

    database.child("Test").setValue(label)
    return label
}

fun onValueChange(context: Context, prediction: String){
    val valueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val value = dataSnapshot.getValue(String::class.java)
            if(value=="Done") {
                makeToast(context, "The Prediction is $prediction", 10000)
                database.child("Test").removeEventListener(this)
            }
            else if(value=="Next"){
                database.child("Test2").get().addOnSuccessListener {
                    val pred = it.value.toString()
                    makeToast(context, "The Prediction is $pred", 10000)
                }
                database.child("Test").removeEventListener(this)
                database.child("Test").setValue("Done")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            makeToast(context, "Error!!!")
        }
    }
    database.child("Test").addValueEventListener(valueEventListener)
}

