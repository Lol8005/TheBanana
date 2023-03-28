package com.banedu.thebanana

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class index : AppCompatActivity(), FileRetriever.ImageDownloadListener{
    //TODO: Added File Retriever
    lateinit var imgBtnPfp: ImageButton
    lateinit var txtUserName: TextView
    lateinit var txtBananaCount: TextView
    lateinit var imgBtnSettings: ImageButton
    lateinit var buttonQuiz: ImageButton
    lateinit var buttonRank: ImageButton
    lateinit var buttonFlashCards: ImageButton
    lateinit var buttonStudyTimer: ImageButton
    private var auth: FirebaseAuth=Firebase.auth
    val uid =auth.currentUser?.uid.toString()
    private val fileRetriever = FileRetriever(uid)
    val database= Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        //TODO: Get uid from auth

        //TODO:Render Pfp uri
        fileRetriever.loadImage(this)

        imgBtnPfp=findViewById(R.id.imgBtnPfp)
        txtUserName=findViewById(R.id.txtUserName)
        txtBananaCount=findViewById(R.id.txtBananaCount)
        imgBtnSettings=findViewById(R.id.imgBtnSettings)
        buttonQuiz=findViewById(R.id.buttonQuiz)
        buttonRank=findViewById(R.id.buttonRank)
        buttonFlashCards=findViewById(R.id.buttonFlashCards)
        buttonStudyTimer=findViewById(R.id.buttonStudyTimer)

        val SLD = SaveLoadData()
        SLD.LoadData(this)

        val clickbuttonSFX: MediaPlayer = MediaPlayer.create(this, AppMediaSound().btnClickSFX)
        clickbuttonSFX.setVolume(SLD.volume.toFloat(), SLD.volume.toFloat())

        val intentFilter = IntentFilter("com.banedu.thebanana.VOLUME_CHANGED")
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "com.banedu.thebanana.VOLUME_CHANGED") {
                    val volume = intent.getIntExtra("volume", 50)
                    clickbuttonSFX.setVolume(volume / 100f, volume / 100f)
                }
            }
        }, intentFilter)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        //Get the username & total banana earned from database and render it.
        //Show UserName & total banana earned
        val RenderHomePage=database.getReference("Users").child(uid)
        RenderHomePage.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    var value1 = snapshot.child("username").value.toString()
                    var value2 = snapshot.child("Total_Banana_Earned").value.toString()
//                    To check got receive the data or not -> check log cat
                    Log.d("Retrieve data", value1)
                    Log.d("Retrieve data", value2)
                    txtBananaCount.text = value2
                    txtUserName.text = value1
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Error", "Failed to read value Username.", error.toException())
            }
        })


        //Shortcuts To Settings
        imgBtnSettings.setOnClickListener {
            clickbuttonSFX.start()

            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }

        //Shortcuts To Quiz Page
        buttonQuiz.setOnClickListener {
            val intent = Intent(this, subjectselection::class.java)
            startActivity(intent)
        }

        //Shortcuts To Ranking Page
        buttonRank.setOnClickListener {
            val intent = Intent(this,rank::class.java)
            startActivity(intent)
        }

        //Shortcuts To Flashcards Page
        buttonFlashCards.setOnClickListener {
            val intent = Intent(this, FlashcardClass::class.java)
            startActivity(intent)
        }

        //Shortcuts To Study Timer Page
        buttonStudyTimer.setOnClickListener {
            val intent = Intent(this,studyTimer::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP

            startActivity(intent)
        }

    }
    override fun onImageDownloaded(uri: Uri?) {
        // Do something with the downloaded URI
        if(uri != null){
            Log.d("URI", uri.toString())

            Glide.with(this)
                .load(uri)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        // Set the Drawable to an ImageView or any other view that accepts a Drawable
                        imgBtnPfp.setImageDrawable(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle any cleanup required when the image is cleared
                    }
                })
        }
    }
}