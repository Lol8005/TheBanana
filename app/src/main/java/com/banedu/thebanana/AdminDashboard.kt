package com.banedu.thebanana

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdminDashboard : AppCompatActivity(), FileRetriever.ImageDownloadListener {

    lateinit var imgBtnPfp: ImageButton
    lateinit var txtUserName: TextView
    lateinit var imgBtnSettings: ImageButton
    lateinit var btnEditQuestion: ImageButton
    lateinit var buttonQuiz: ImageButton

    private var auth: FirebaseAuth = Firebase.auth
    val uid = auth.currentUser?.uid.toString()

    private val fileRetriever = FileRetriever(uid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        fileRetriever.loadImage(this)

        imgBtnPfp = findViewById(R.id.imgBtnPfp)
        txtUserName = findViewById(R.id.txtUserName)
        imgBtnSettings = findViewById(R.id.imgBtnSettings)
        btnEditQuestion = findViewById(R.id.btnEditQuestion)
        buttonQuiz=findViewById(R.id.buttonQuiz)

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

        txtUserName.text = SLD.username


        //Shortcuts To Settings
        imgBtnSettings.setOnClickListener {
            clickbuttonSFX.start()

            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }

        //Shortcuts To Quiz Page
        btnEditQuestion.setOnClickListener {
            val intent = Intent(this, SelectTopic::class.java)
            startActivity(intent)
        }

        buttonQuiz.setOnClickListener{
            val intent = Intent(this, subjectselection::class.java)
            startActivity(intent)
        }

        imgBtnPfp.setOnClickListener {
            startActivity(Intent(this, UserProfile::class.java))
            overridePendingTransition(0, 0) //Remove transition animation
        }
    }

    override fun onBackPressed() {
        return
    }

    override fun onImageDownloaded(uri: Uri?) {
        // Do something with the downloaded URI
        if (uri != null) {
            Log.d("URI", uri.toString())

            Glide.with(this)
                .load(uri)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
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