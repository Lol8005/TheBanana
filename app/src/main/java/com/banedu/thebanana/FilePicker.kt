package com.banedu.thebanana

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*


class FilePicker(private val activity: ComponentActivity) {

    interface ImageUploadListener {
        fun onImageUploaded(uri: Uri)
    }

    private var uid = Firebase.auth.currentUser?.uid.toString()

    lateinit var listener: ImageUploadListener

    private val pickFileLauncher = activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri ->
            val storageRef = DB_Connection().connectStorageDB()
            val imagesRef = storageRef.child("images/${uid}")
            val uploadTask = imagesRef.putFile(imageUri)

            uploadTask.addOnSuccessListener {taskSnapshot ->
                // Handle successful upload

                listener.onImageUploaded(uri)

            }.addOnFailureListener {
                // Handle upload failure
            }
        }
    }

    fun pickFile(_listener: ImageUploadListener) {
        listener = _listener

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }

        pickFileLauncher.launch("image/*")
    }

    // Call registerForActivityResult in onCreate or onStart method
    init {
        pickFileLauncher
    }
}
