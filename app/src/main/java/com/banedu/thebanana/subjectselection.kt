package com.banedu.thebanana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class subjectselection : AppCompatActivity() {

    private lateinit var startquizButton: Button
    lateinit var btnBackHomeFromSS: ImageButton

    lateinit var subjectRadioRV: RecyclerView

    lateinit var topicSelectionRVAdapter: TopicSelectionRVAdapter
    var topicRecord = ArrayList<TopicRecordFormat>()

    private lateinit var auth: FirebaseAuth
    private lateinit var DB_Reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjectselection)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        auth = Firebase.auth
        DB_Reference = DB_Connection().connectRealDB()

        subjectRadioRV = findViewById(R.id.subjectRadioRV)

        subjectRadioRV.layoutManager = LinearLayoutManager(this)
        topicSelectionRVAdapter = TopicSelectionRVAdapter(topicRecord)
        subjectRadioRV.adapter = topicSelectionRVAdapter

        startquizButton = findViewById(R.id.startquizButton)
        btnBackHomeFromSS = findViewById(R.id.btnBackHomeFromSS)

        DB_Reference.child("Topic").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                topicRecord.clear()
                for (uid in snapshot.children) {
                    for (questionID in uid.children) {
                        //check if question exist
                        if (questionID.child("0").exists()) {
                            topicRecord.add(
                                TopicRecordFormat(
                                    questionID.key.toString(),
                                    questionID.child("TopicName").value.toString(),
                                    uid.key.toString()
                                )
                            )
                        }
                    }
                }

                topicSelectionRVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        btnBackHomeFromSS.setOnClickListener {
            finish()
        }

        startquizButton.setOnClickListener {
//            val selectedSubjectId = subjectRadioGroup.checkedRadioButtonId
//            Log.d("radio ID", selectedSubjectId.toString())
//
//            if (selectedSubjectId != -1) {
////                val selectedSubject = findViewById<RadioButton>(selectedSubjectId)
////                val intent = Intent(this, quiz::class.java)
////                intent.putExtra("selected_subject", selectedSubject.text.toString())
////                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Please select a subject", Toast.LENGTH_SHORT).show()
//            }
            val selectedSubjectId = topicSelectionRVAdapter.getSelectedItemId()

            if (selectedSubjectId != "") {
                val intent = Intent(this, quiz::class.java)
                intent.putExtra("selected_subject", selectedSubjectId)
                intent.putExtra("authorUID", topicSelectionRVAdapter.getSelectedItemAuthor())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a subject", Toast.LENGTH_SHORT).show()
            }

        }
    }
}