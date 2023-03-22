package com.banedu.thebanana

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DB_Connection {

    private lateinit var firebaseRealDB: FirebaseDatabase
    private lateinit var RealDB_Reference: DatabaseReference

    var RealDB_path = "https://thebanana-537eb-default-rtdb.firebaseio.com/"


    private lateinit var firebaseStorageDB: FirebaseStorage
    private lateinit var StorageDB_Reference: StorageReference

    var StorageDB_path = "gs://thebanana-537eb.appspot.com"

    fun connectRealDB(): DatabaseReference{
        firebaseRealDB = Firebase.database(RealDB_path)
        RealDB_Reference = firebaseRealDB.reference

        return RealDB_Reference
    }

    fun connectStorageDB(): StorageReference{
        firebaseStorageDB = FirebaseStorage.getInstance(StorageDB_path)
        StorageDB_Reference = firebaseStorageDB.reference

        return StorageDB_Reference
    }
}