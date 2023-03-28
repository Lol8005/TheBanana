package com.banedu.thebanana

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FileRetriever(var uid:String) {
    interface ImageDownloadListener {
        fun onImageDownloaded(uri: Uri)
    }

//    private var uid = Firebase.auth.currentUser?.uid.toString()

    fun loadImage(listener: ImageDownloadListener){
        val storageRef = DB_Connection().connectStorageDB()
        val imagesRef = storageRef.child("images/$uid")

        imagesRef.downloadUrl.addOnSuccessListener { uri ->
            // Image download successful
            // Invoke the listener callback with the downloaded URI
            listener.onImageDownloaded(uri)
        }
    }
}