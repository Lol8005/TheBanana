package com.banedu.thebanana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import layout.CourseRvModal

class index : AppCompatActivity() {

    lateinit var imgBtnPfp: ImageButton
    lateinit var txtUserName: TextView
    lateinit var txtBananaCount: TextView
    lateinit var imgBtnSettings: ImageButton
    lateinit var buttonQuiz: ImageButton
    lateinit var buttonRank: ImageButton
    lateinit var buttonFlashCards: ImageButton
    lateinit var buttonStudyTimer: ImageButton
    //TODO: define auth & storage

//    lateinit var auth: FirebaseAuth
    val database= Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        //TODO: Get uid from auth
//        val uid =auth.currentUser?.uid
        //TODO:How to get Pfp uri
//        val pfpURI=auth.currentUser?.pfpURI

        //TODO: Define Reference for database to get username and banana count.
        //Get username & Banana count
//        val RenderHomepage=database.getReference("User").child(uid.toString())

        imgBtnPfp=findViewById(R.id.imgBtnPfp)
        txtUserName=findViewById(R.id.txtUserName)
        txtBananaCount=findViewById(R.id.txtBananaCount)
        imgBtnSettings=findViewById(R.id.imgBtnSettings)
        buttonQuiz=findViewById(R.id.buttonQuiz)
        buttonRank=findViewById(R.id.buttonRank)
        buttonFlashCards=findViewById(R.id.buttonFlashCards)
        buttonStudyTimer=findViewById(R.id.buttonStudyTimer)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        //TODO: Get the username from database and render it. Profile pic too.
        //Show UserName & PFP
//        RenderHomePage.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()) {
//                    var value1 = snapshot.child("Username").getValue().toString()
                    //To check got receive the data or not -> check log cat
//                    Log.d("Retrieve data", value1)
//                    txtUserName.setText(username)
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("Error", "Failed to read value Username.", error.toException())
//            }
//        })

        //TODO: Get the banana Count from database and render it.
        //Show Banana Count
//        RenderHomePage.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()) {
//                    var value2 = snapshot.child("Total_Banana_Earned").getValue().toString()
                        //To check got receive the data or not -> check log cat
//                    Log.d("Retrieve data", value2)
//                    txtBananaCount.setText(value2)
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("Error", "Failed to read value Banana Count.", error.toException())
//            }
//        })

        //Shortcuts To Settings
        imgBtnSettings.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }

        //Shortcuts To Quiz Page
//        buttonQuiz.setOnClickListener {
//            val intent = Intent(this,quiz::class.java)
//            startActivity(intent)
//        }

        //Shortcuts To Ranking Page
        buttonRank.setOnClickListener {
            val intent = Intent(this,rank::class.java)
            startActivity(intent)
        }

        //Shortcuts To Flashcards Page
//        buttonFlashCards.setOnClickListener {
//            val intent = Intent(this,flashcards::class.java)
//            startActivity(intent)
//        }

        //Shortcuts To Study Timer Page
        buttonStudyTimer.setOnClickListener {
            val intent = Intent(this,studyTimer::class.java)
            startActivity(intent)
        }

    }
}