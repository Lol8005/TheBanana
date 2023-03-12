package com.example.stiventure

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DB_Connection {

    private lateinit var firebaseDB: FirebaseDatabase
    private lateinit var DB_Reference: DatabaseReference

    var DB_path = "https://stiventure-default-rtdb.firebaseio.com/"

    fun connect(): DatabaseReference{
        firebaseDB = Firebase.database(DB_path)
        DB_Reference = firebaseDB.reference

        return DB_Reference
    }
}