package com.banedu.thebanana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class subjectselection : AppCompatActivity() {

    private lateinit var startquizButton: Button
    lateinit var btnBackHomeFromSS: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjectselection)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        startquizButton = findViewById<Button>(R.id.startquizButton)
        btnBackHomeFromSS = findViewById(R.id.btnBackHomeFromSS)
        val subjectRadioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        btnBackHomeFromSS.setOnClickListener {
            val intent = Intent(this, index::class.java)
            startActivity(intent)
        }

        startquizButton.setOnClickListener {
            val selectedSubjectId = subjectRadioGroup.checkedRadioButtonId
            if (selectedSubjectId != -1) {
                val selectedSubject = findViewById<RadioButton>(selectedSubjectId)
                val intent = Intent(this, quiz::class.java)
                intent.putExtra("selected_subject", selectedSubject.text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a subject", Toast.LENGTH_SHORT).show()
            }
        }
    }
}