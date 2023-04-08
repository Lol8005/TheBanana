package com.banedu.thebanana

import android.net.Uri
import android.util.Log

class FileRetriever(private var uid: String) {
    interface ImageDownloadListener {
        fun onImageDownloaded(uri: Uri?)
    }

    fun loadImage(listener: ImageDownloadListener) {
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