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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        scoreLabel = findViewById(R.id.scoreLabel)
        scoreValue = findViewById(R.id.scoreValue)
        btnBackHomeFromRes = findViewById(R.id.btnBackHomeFromRes)

        //Setup Back Button
        btnBackHomeFromRes.setOnClickListener {
            val intent = Intent(this, index::class.java)
            startActivity(intent)
        }

        //Get score from Quiz page
        // "0" is the default value if the key is not found in the Intent
        val score = intent.getIntExtra("score", 0)

        //Set text for scoreValue to be score/10
        scoreValue.text = "$score/10"
    }
}