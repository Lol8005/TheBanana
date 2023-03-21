package com.banedu.thebanana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.core.text.isDigitsOnly
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import com.banedu.thebanana.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Runnable
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.time.LocalDate
import java.time.Period
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class studyTimer : AppCompatActivity() {

    //region declare variable

    lateinit var runnable: Runnable
    lateinit var btnBackHomeFromST: ImageButton
    lateinit var edtSetTime: EditText
    lateinit var btnSetTimer: Button
    lateinit var txtTimer: TextView
    lateinit var btnStartTimer: Button
    lateinit var txtMusicTitle: TextView
    lateinit var imgMusicCover: ImageView
    lateinit var seekbar: SeekBar
    lateinit var btnPreMusic: ImageButton
    lateinit var btnPlayMusic: ImageButton
    lateinit var btnNextMusic: ImageButton
    lateinit var countdown_timer: CountDownTimer
    lateinit var viewKonfetti: KonfettiView
    lateinit var period: Period
    lateinit var txtTotalTimeToday: TextView
    lateinit var txtTotalTimeSevenDays: TextView
    lateinit var btnGenerate: Button
    var time = 0
    var time_in_ms = 0L
    var errorMsg = ""
    var isRunning: Boolean = false
    val current = LocalDate.now()
    var handler = Handler()
    var currentMusic = 0
    var total = 0
    var musicArray: ArrayList<musicClass> = ArrayList()

    lateinit var mediaplayer: MediaPlayer

    //TODO: define auth & storage
    lateinit var auth: FirebaseAuth
    lateinit var DB_Reference: DatabaseReference

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_timer)

        //region Initialize Variable

        btnBackHomeFromST = findViewById(R.id.btnBackHomeFromST)
        edtSetTime = findViewById(R.id.edtSetTime)
        btnSetTimer = findViewById(R.id.btnSetTimer)
        txtTimer = findViewById(R.id.txtTimer)
        btnStartTimer = findViewById(R.id.btnStartTimer)
        txtMusicTitle = findViewById(R.id.txtMusicTitle)
        imgMusicCover = findViewById(R.id.imgMusicCover)
        seekbar = findViewById(R.id.seekBar)
        btnPreMusic = findViewById(R.id.btnPreMusic)
        btnPlayMusic = findViewById(R.id.btnPlayMusic)
        btnNextMusic = findViewById(R.id.btnNextMusic)
        viewKonfetti = findViewById(R.id.viewKonfetti)
        txtTotalTimeToday = findViewById(R.id.txtTotalTimeToday)
        txtTotalTimeSevenDays = findViewById(R.id.txtTotalTimeSevenDays)
        btnGenerate = findViewById(R.id.btnGenerate)

        auth = Firebase.auth
        DB_Reference = DB_Connection().connect()

        //TODO: Get uid from auth
        val uid = auth.currentUser?.uid.toString()
        var databaseRefTimeNow = Firebase.database.getReference("Study Time Record").child(uid).child(current.toString())

        //endregion

        //region Hide toolbar

        var HSB: HideSystemBar = HideSystemBar()
        HSB.hide(window)

        //endregion

        //region set music

        musicArray.add(musicClass("Relax.mp3", R.raw.relax_music, R.drawable.flowericon))
        musicArray.add(musicClass("Banana.mp3", R.raw.bananasong, R.drawable.banana))
        musicArray.add(musicClass("MinionOpera.mp3", R.raw.minionopera, R.drawable.minion))

        txtMusicTitle.setText(musicArray[0].title)
        mediaplayer = MediaPlayer.create(this, musicArray[0].music)
        imgMusicCover.setImageResource(musicArray[0].image)
        seekbar.progress = 0
        seekbar.max = mediaplayer.duration

        //endregion

        btnBackHomeFromST.setOnClickListener {
            val intent = Intent(this, index::class.java)
            startActivity(intent)
        }

        //region Time Button
        btnStartTimer.visibility = View.INVISIBLE
//        TODO: Set Timer
//        TODO: Receive time length
//        TODO: SET TEXT FOR COUNTDOWN
        btnSetTimer.setOnClickListener {
            if (getInputTime() == 0) {
                var initialHour = time / 60 / 60
                var initialMinutes = time / 60 % 60
                var initialSeconds = time % 60
                txtTimer.setText(String.format("%02d:%02d:%02d", initialHour, initialMinutes, initialSeconds))
                btnStartTimer.visibility = View.VISIBLE
                edtSetTime.setText("")
            } else {
                Toast.makeText(this, "Sorry!" + errorMsg, Toast.LENGTH_LONG).show()
            }

        }

//      TODO: ONCE START BUTTON IS CLICKED, TIMER STARTS
        btnStartTimer.setOnClickListener {
            if (isRunning) {
                //TODO: IF USER GAVE UP, TIMER RESET
                resetTimer()
                Toast.makeText(this, "Sorry! Try harder next time.", Toast.LENGTH_LONG).show()
            } else {
                btnStartTimer.setText("GIVE UP?")
                time_in_ms = time * 1000L
                startTimer(time_in_ms)
                countdown_timer = object : CountDownTimer(time_in_ms, 1000) {
                    override fun onFinish() {
                        loadConfeti()
                        resetTimer()
                        var add = true
                        databaseRefTimeNow.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (add == true) {
                                    if (snapshot.exists()) {
                                        var value2 = snapshot.child("study_time_length").value
                                            .toString().toInt()
                                        total = (value2 + (time / 60))
                                        Log.d("TAG", value2.toString())
                                        Log.d("TAG", (time / 60).toString())
                                        Log.d("TAG", total.toString())
                                        Log.d("TAG", "Add to previous record")
                                        //TODO: IF RECORD NOT EXISTS (THIS WANT STILL NEED TO BE TESTED FIRST)
                                    } else {
                                        total = time / 60
                                        Log.d("TAG", "Add new record" + (total).toString())
                                    }
                                    databaseRefTimeNow.child("study_time_length")
                                        .setValue(total.toString())
                                }
                                add = false
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.w("ERROR", "Failed to read value.", error.toException())
                            }
                        })

                    }

                    override fun onTick(p0: Long) {
                        time_in_ms = p0
                        updateTextUI()
                    }
                }
                countdown_timer.start()

                isRunning = true

            }
        }
        //endregion

//      TODO: Set Music Player(3 TRACKS)
        //region Music

        //TODO: CANNOT AUTOPLAY, SOLVE
        mediaplayer.setOnCompletionListener {
            playnextmusic()
        }

        btnPlayMusic.setOnClickListener {
            playcurrentmusic()
        }

        btnPreMusic.setOnClickListener {
            playpreviousmusic()
        }

        btnNextMusic.setOnClickListener {
            playnextmusic()
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if (changed) {
                    mediaplayer.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        runnable = Runnable {
            seekbar.progress = mediaplayer.currentPosition
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)

        //endregion

//      TODO: Statistic
//      TODO: Total Today & Total Accumulated
        //region Stats
        btnGenerate.setOnClickListener {

            DB_Reference.child("Study Time Record").child(uid).child(current.toString())
                .child("study_time_length").get().addOnSuccessListener {
                txtTotalTimeToday.setText("${it.value.toString()} minutes")
            }

            var totalTimeStudy = 0

            DB_Reference.child("Study Time Record").child(uid).get().addOnSuccessListener {
                if(it.value != null){
                    for (day in 0..6) {
                        period = Period.of(0, 0, day)

                        if(it.child(current.minus(period).toString()).child("study_time_length").value != null){
                            totalTimeStudy += it.child(current.minus(period).toString()).child("study_time_length").value.toString().toInt()
                        }
                    }

                    txtTotalTimeSevenDays.setText("$totalTimeStudy minutes")
                }
            }
        }

        //endregion
    }

    //    To validate Time entered
    fun getInputTime(): Int {
        var retu = 0
        var temptime = edtSetTime.text.toString().trim()

        if (temptime.isEmpty() or !temptime.isDigitsOnly()) {
            errorMsg = "This field cannot be empty or contain alphabet"
            retu = 1
        } else if ((temptime.toInt() > 120) or (temptime.toInt() < 30)) {
            errorMsg = "Maximum 120 Minutes. Minimum 30 Minutes."
            retu = 1
        } else {
            time = temptime.toInt() * 60
        }
        return retu
    }

    //TODO: IF SUCCEED, ADD RECORD
    //TODO: Add record
    //    To start Timer
    fun startTimer(time_in_seconds: Long) {


    }

    //    To reset timer
    fun resetTimer() {
        time_in_ms = 0L
        updateTextUI()
        isRunning = false
        countdown_timer.cancel()
        btnStartTimer.visibility = View.INVISIBLE
        btnStartTimer.setText("START!")
    }

    //    To update the countdown number
    fun updateTextUI() {
        val hour = (time_in_ms / 1000) / 60 / 60
        val minute = (time_in_ms / 1000) / 60 % 60
        val seconds = (time_in_ms / 1000) % 60

        txtTimer.setText(String.format("%02d:%02d:%02d", hour, minute, seconds))
    }

    //    To run confetti
    fun loadConfeti() {
        var party = Party(
            speed = 5f,
            maxSpeed = 20f,
            damping = 0.9f,
            spread = 360,
            fadeOutEnabled = true,
            colors = listOf(Color.YELLOW, Color.GREEN, Color.MAGENTA),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        viewKonfetti.start(party)
    }

    fun playcurrentmusic() {
        if (!mediaplayer.isPlaying) {
            mediaplayer.start()
            btnPlayMusic.setImageResource(R.drawable.pausemusicicon)
        } else {
            mediaplayer.pause()
            btnPlayMusic.setImageResource(R.drawable.playmusicicon)
        }
    }

    fun playnextmusic() {
        if (musicArray.size > currentMusic + 1) {
            mediaplayer.stop()
            mediaplayer.release()

            currentMusic++
            txtMusicTitle.setText(musicArray[currentMusic].title)
            mediaplayer = MediaPlayer.create(this, musicArray[currentMusic].music)
            imgMusicCover.setImageResource(musicArray[currentMusic].image)

            mediaplayer.setOnCompletionListener {
                playnextmusic()
            }

            seekbar.progress = 0
            seekbar.max = mediaplayer.duration
            mediaplayer.start()
            btnPlayMusic.setImageResource(R.drawable.pausemusicicon)

            Log.d("Music", "${musicArray.size} $currentMusic")
        } else {
            Toast.makeText(this, "No more music to play", Toast.LENGTH_LONG).show()
        }
    }

    fun playpreviousmusic() {
        if (currentMusic > 0) {
            currentMusic--
            mediaplayer.stop()
            mediaplayer.release()

            txtMusicTitle.setText(musicArray[currentMusic].title)
            mediaplayer = MediaPlayer.create(this, musicArray[currentMusic].music)
            imgMusicCover.setImageResource(musicArray[currentMusic].image)

            mediaplayer.setOnCompletionListener {
                playnextmusic()
            }

            seekbar.progress = 0
            seekbar.max = mediaplayer.duration
            mediaplayer.start()
            btnPlayMusic.setImageResource(R.drawable.pausemusicicon)
        } else {
            Toast.makeText(this, "Can't play previous music", Toast.LENGTH_LONG).show()
        }
    }
}


data class musicClass(var title: String, var music: Int, var image: Int)