package com.banedu.thebanana

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class EditTopic : AppCompatActivity(), questionRecordListener {

    lateinit var questionCV: RecyclerView
    lateinit var questionRecordRVAdapter: QuestionRecordRVAdapter
    var questionRecord = ArrayList<QuestionRecordFormat>()

    lateinit var btn_return: ImageButton
    lateinit var btnAddQuestion: ImageButton
    lateinit var btnSaveTopic: ImageButton

    lateinit var edtTopicName: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var DB_Reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_topic)

        btnAddQuestion = findViewById(R.id.btnDoneEditQuestion)
        btnSaveTopic = findViewById(R.id.btnSaveTopic)
        btn_return = findViewById(R.id.btn_return)

        edtTopicName = findViewById(R.id.edtQuestion)

        auth = Firebase.auth
        DB_Reference = DB_Connection().connectRealDB()

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val questionClass =
                        result.data?.getParcelableExtra<QuestionRecordFormat>("QuestionRecordFormat")

                    if (questionClass != null) {
                        questionRecord.add(questionClass)

                        questionRecordRVAdapter.notifyDataSetChanged()
                    }
                }
            }

        var passedRecord: TopicRecordFormat? = null
        passedRecord = intent.getParcelableExtra("topicRecord") as TopicRecordFormat?

        if (passedRecord != null) {
            DB_Reference.child("Topic").child(auth.uid.toString()).child(passedRecord.topicID).get()
                .addOnSuccessListener {
                    edtTopicName.setText(it.child("TopicName").value.toString())

                    addQuestionRecordsFromFirebase(it)
                }
        }


        questionCV = findViewById(R.id.questionCV)
        questionCV.layoutManager = LinearLayoutManager(this)
        questionRecordRVAdapter = QuestionRecordRVAdapter(questionRecord, this, resultLauncher)
        questionCV.adapter = questionRecordRVAdapter

        btn_return.setOnClickListener {
            finish()
        }

        btnAddQuestion.setOnClickListener {
            val intent = Intent(this, EditQuestion::class.java)
            resultLauncher.launch(intent)
        }

        btnSaveTopic.setOnClickListener {
            //check got question or not
            if (edtTopicName.text.isEmpty()) {
                Toast.makeText(this, "Please enter topic name", Toast.LENGTH_SHORT).show()
            } else {
                if (passedRecord?.topicID == null) {
                    val uid = auth.uid.toString()
                    var path = DB_Reference.child("Topic").child(uid).push()

                    path.child("TopicName").setValue(edtTopicName.text.toString())

                    for (index in questionRecord) {
                        val pos = questionRecord.indexOf(index).toString()
                        path.child(pos).child("question").setValue(index.question)
                        path.child(pos).child("correctAns").setValue(index.correctAns)
                        path.child(pos).child("wrongAns1").setValue(index.wrongAns1)
                        path.child(pos).child("wrongAns2").setValue(index.wrongAns2)
                    }
                } else {
                    val uid = auth.uid.toString()
                    var path = DB_Reference.child("Topic").child(uid).child(passedRecord.topicID)

                    path.child("TopicName").setValue(edtTopicName.text.toString())

                    for (index in questionRecord) {
                        val pos = questionRecord.indexOf(index).toString()
                        path.child(pos).child("question").setValue(index.question)
                        path.child(pos).child("correctAns").setValue(index.correctAns)
                        path.child(pos).child("wrongAns1").setValue(index.wrongAns1)
                        path.child(pos).child("wrongAns2").setValue(index.wrongAns2)
                    }
                }

                finish()
            }
        }
    }

    override fun returnQuestionRecord(record: QuestionRecordFormat) {
        questionRecord.remove(record)
    }

    private fun addQuestionRecordsFromFirebase(snapshot: DataSnapshot) {
        for (index in snapshot.children) {
            if (index.key.toString() != "TopicName") {
                val record = QuestionRecordFormat(
                    index.child("question").value.toString(),
                    index.child("correctAns").value.toString(),
                    index.child("wrongAns1").value.toString(),
                    index.child("wrongAns2").value.toString()
                )
                questionRecord.add(record)
            }
        }
        questionRecordRVAdapter.notifyDataSetChanged()
    }
}

data class QuestionRecordFormat(
    var question: String,
    var correctAns: String,
    var wrongAns1: String,
    var wrongAns2: String
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeString(correctAns)
        parcel.writeString(wrongAns1)
        parcel.writeString(wrongAns2)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuestionRecordFormat> {
        override fun createFromParcel(parcel: Parcel): QuestionRecordFormat {
            return QuestionRecordFormat(parcel)
        }

        override fun newArray(size: Int): Array<QuestionRecordFormat?> {
            return arrayOfNulls(size)
        }
    }
}