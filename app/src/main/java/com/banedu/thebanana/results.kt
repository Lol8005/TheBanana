package com.banedu.thebanana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class results : AppCompatActivity() {

    private lateinit var scoreLabel: TextView
    private lateinit var scoreValue: TextView
    private lateinit var btnBackHomeFromRes: ImageButton
    private lateinit var remarkstextView: TextView

    lateinit var SLD: SaveLoadData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        scoreLabel = findViewById(R.id.scoreLabel)
        scoreValue = findViewById(R.id.scoreValue)
        btnBackHomeFromRes = findViewById(R.id.btnBackHomeFromRes)
        remarkstextView = findViewById(R.id.remarkstextView)

        SLD = SaveLoadData()
        SLD.LoadData(this)

        //Setup Back Button
        btnBackHomeFromRes.setOnClickListener {
            if(SLD.role == "student"){
                val intent = Intent(this, index::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, AdminDashboard::class.java)
                startActivity(intent)
            }

            finish()
        }

        //Get score from Quiz page
        // "0" is the default value if the key is not found in the Intent
        val score = intent.getIntExtra("score", 0)
        val totalQuestion = intent.getIntExtra("totalQuestion", 0)

        //Set text for scoreValue to be score/10
        scoreValue.text = "$score / $totalQuestion"

        //Set remarks on users performance
        if (score == totalQuestion) {
            remarkstextView.text = "Congratulations!! You're doing great!!!"
        } else if (score >= totalQuestion * .7) {
            remarkstextView.text = "You did great!!, be careful next time and you might get them all right."
        } else if (score >= totalQuestion * .4) {
            remarkstextView.text = "Try harder next time..."
        } else {
            remarkstextView.text = "You should study your textbook more..."
        }
    }
}