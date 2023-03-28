package com.banedu.thebanana

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import java.net.HttpURLConnection
import java.net.URL

class FileRetriever(private var uid:String) {
    interface ImageDownloadListener {
        fun onImageDownloaded(uri: Uri?)
    }

    fun loadImage(listener: ImageDownloadListener){
        val storageRef = DB_Connection().connectStorageDB()
        val imagesRef = storageRef.child("images")

        imagesRef.listAll().addOnSuccessListener { listResult ->
            for (item in listResult.items) {
                if (item.name == uid) {
                    // File exists, do something with it
                    Log.d("Exist", "$uid")

                    imagesRef.child(uid).downloadUrl.addOnSuccessListener { uri ->
                        // Image download successful
                        // Invoke the listener callback with the downloaded URI

                        listener.onImageDownloaded(uri)
                    }

                    break
                }
            }
        }.addOnFailureListener { exception ->
            // Handle any errors
        }
    }
}