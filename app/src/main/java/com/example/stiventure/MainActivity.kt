package com.example.stiventure

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : AppCompatActivity() {

    lateinit var btn_play: ImageButton
    lateinit var btn_setting: ImageButton
    lateinit var btn_exit: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //region Hide toolbar

        var HSB: HideSystemBar = HideSystemBar()
        HSB.hide(window)

        //endregion

        checkPlayerLogin()

        btn_play = findViewById(R.id.btn_play)
        btn_setting = findViewById(R.id.btn_setting)
        btn_exit = findViewById(R.id.btn_exit)

        val clickbuttonSFX: MediaPlayer = MediaPlayer.create(this, R.raw.clickbuttonsfx)

        btn_play.setOnClickListener{
            clickbuttonSFX.start()
        }

        btn_setting.setOnClickListener{

            clickbuttonSFX.start()

            var intent = Intent(this, Setting::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0) //Remove transition animation

        }

        btn_exit.setOnClickListener{

            clickbuttonSFX.start()

            finish()
        }
    }

    fun checkPlayerLogin(){
        startActivity(Intent(this, LoginAccount::class.java))
        overridePendingTransition(0, 0) //Remove transition animation
    }
}