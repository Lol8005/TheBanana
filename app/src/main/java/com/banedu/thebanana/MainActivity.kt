package com.banedu.thebanana

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var btn_play: ImageButton
    lateinit var btn_setting: ImageButton
    lateinit var btn_profile: ImageButton
    lateinit var btn_exit: ImageButton

    lateinit var SLD: SaveLoadData

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //region Hide toolbar

        var HSB: HideSystemBar = HideSystemBar()
        HSB.hide(window)

        //endregion

        checkPlayerLogin()

        //region Initialize Variable

        btn_play = findViewById(R.id.btn_play)
        btn_setting = findViewById(R.id.btn_setting)
        btn_profile = findViewById(R.id.btn_profile)
        btn_exit = findViewById(R.id.btn_exit)

        SLD = SaveLoadData()
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

        //endregion

        //region Button On Click Listener

        btn_play.setOnClickListener{
            clickbuttonSFX.start()

            startActivity(Intent(this, index::class.java))
            overridePendingTransition(0, 0) //Remove transition animation
        }

        btn_setting.setOnClickListener{
            clickbuttonSFX.start()

            startActivity(Intent(this, Setting::class.java))
            overridePendingTransition(0, 0) //Remove transition animation
        }

        btn_profile.setOnClickListener{
            clickbuttonSFX.start()

            startActivity(Intent(this, UserProfile::class.java))
            overridePendingTransition(0, 0) //Remove transition animation
        }

        btn_exit.setOnClickListener{
            clickbuttonSFX.start()

            finish()
        }

        //endregion
    }

    fun checkPlayerLogin(){
        startActivity(Intent(this, LoginAccountFirebase::class.java))
        overridePendingTransition(0, 0) //Remove transition animation
    }

    //disable back button from killing app
    override fun onBackPressed() {
        return
    }
}