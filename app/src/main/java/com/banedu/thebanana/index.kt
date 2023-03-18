package com.banedu.thebanana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class index : AppCompatActivity() {

    lateinit var imgBtnPfp: ImageButton
    lateinit var txtUserName: TextView
    lateinit var txtBananaCount: TextView
    lateinit var imgBtnSettings: ImageButton
    lateinit var buttonQuiz: ImageButton
    lateinit var buttonRank: ImageButton
    lateinit var buttonFlashCards: ImageButton
    lateinit var buttonStudyTimer: ImageButton
    val database= Firebase.database

    //TODO: define auth & storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        val intent=getIntent()

        //TODO: Get uid from auth
//        val uid =auth.currentUser?.uid
        //TODO: Define Reference for database <username>.
        //Get username
//        val RenderUserName=database.getReference().child("1")

        //TODO: Define Reference for database <bananaCount>.
        //Get bananaCount

        imgBtnPfp=findViewById(R.id.imgBtnPfp)
        txtUserName=findViewById(R.id.txtUserName)
        txtBananaCount=findViewById(R.id.txtBananaCount)
        imgBtnSettings=findViewById(R.id.imgBtnSettings)
        buttonQuiz=findViewById(R.id.buttonQuiz)
        buttonRank=findViewById(R.id.buttonRank)
        buttonFlashCards=findViewById(R.id.buttonFlashCards)
        buttonStudyTimer=findViewById(R.id.buttonStudyTimer)

        //TODO: Get the username from database and render it. Profile pic too.
        //Show UserName & PFP
//        RenderUserName.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()) {
//                    var value1 = snapshot.child("Name").getValue()
                    //To check got receive the data or not -> check log cat
//                    Log.d("TAG", value1.toString())
//                    txtUserName.setText(username)
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("TAG", "Failed to read value Username.", error.toException())
//            }
//        })

        //TODO: Get the banana Count from database and render it.
        //Show Banana Count
//        RenderBananaCount.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()) {
//                    var value1 = snapshot.child("Name").getValue()
            //To check got receive the data or not -> check log cat
//                    Log.d("TAG", value1.toString())
//                    txtUserName.setText(value1.toString())
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("TAG", "Failed to read value Username.", error.toException())
//            }
//        })

        //Shortcuts To Settings
//        imgBtnSettings.setOnClickListener {
//            val intent = Intent(this,settings::class.java)
//            startActivity(intent)
//        }

        //Shortcuts To Quiz Page
//        buttonQuiz.setOnClickListener {
//            val intent = Intent(this,quiz::class.java)
//            startActivity(intent)
//        }

        //Shortcuts To Ranking Page
//        buttonRank.setOnClickListener {
//            val intent = Intent(this,rank::class.java)
//            startActivity(intent)
//        }

        //Shortcuts To Flashcards Page
//        buttonFlashCards.setOnClickListener {
//            val intent = Intent(this,flashcards::class.java)
//            startActivity(intent)
//        }

        //Shortcuts To Study Timer Page
//        buttonStudyTimer.setOnClickListener {
//            val intent = Intent(this,studyTimer::class.java)
//            startActivity(intent)
//        }

    }
}