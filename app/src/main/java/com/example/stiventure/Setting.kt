package com.example.stiventure

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar

class Setting : AppCompatActivity() {

    lateinit var btn_close_setting: ImageButton

    lateinit var btn_volume: ImageButton
    lateinit var seekBar_volume: SeekBar

    lateinit var btn_music: ImageButton
    lateinit var seekBar_music: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        //region Hide toolbar

        var HSB: HideSystemBar = HideSystemBar()
        HSB.hide(window)

        //endregion

        //region Initialize Data

        btn_close_setting = findViewById(R.id.btn_close_setting)

        btn_volume = findViewById(R.id.btn_volume)
        seekBar_volume = findViewById(R.id.seekBar_volume)

        btn_music = findViewById(R.id.btn_music)
        seekBar_music = findViewById(R.id.seekBar_music)

        val clickbuttonSFX: MediaPlayer = MediaPlayer.create(this, R.raw.clickbuttonsfx)

        //endregion

        //region Set Setting Value

        var SLD = SaveLoadData()
        SLD.LoadData(applicationContext)

        seekBar_volume.progress = SLD.volume
        seekBar_music.progress = SLD.music

        updateViewsAfterLoadData()

        //endregion

        btn_volume.setOnClickListener{
            if(seekBar_volume.progress == 0){
                seekBar_volume.progress = 100
            }else{
                seekBar_volume.progress = 0
            }

            SLD.volume = seekBar_volume.progress
            SLD.SaveData(applicationContext)

            clickbuttonSFX.start()
            updateViewsAfterLoadData()
        }

        seekBar_volume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                updateViewsAfterLoadData()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                SLD.volume = seekBar_volume.progress

                SLD.SaveData(applicationContext)
            }
        })

        btn_music.setOnClickListener{
            if(seekBar_music.progress == 0){
                seekBar_music.progress = 100
            }else{
                seekBar_music.progress = 0
            }

            SLD.music = seekBar_music.progress
            SLD.SaveData(applicationContext)

            clickbuttonSFX.start()
            updateViewsAfterLoadData()
        }

        seekBar_music.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                updateViewsAfterLoadData()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // you can probably leave this empty
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                SLD.music = seekBar_music.progress

                SLD.SaveData(applicationContext)
            }
        })

        btn_close_setting.setOnClickListener{

            clickbuttonSFX.start()

            finish()
            overridePendingTransition(0, 0)
        }
    }

    fun updateViewsAfterLoadData(){
        if(seekBar_volume.progress == 0){
            btn_volume.setImageResource(R.drawable.volumedisableicon)
        }else{
            btn_volume.setImageResource(R.drawable.volumeicon)
        }

        if(seekBar_music.progress == 0){
            btn_music.setImageResource(R.drawable.musicdisableicon)
        }else{
            btn_music.setImageResource(R.drawable.musicicon)
        }
    }
}