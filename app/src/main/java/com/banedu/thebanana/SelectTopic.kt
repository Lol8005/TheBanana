package com.banedu.thebanana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class SelectTopic : AppCompatActivity() {

    lateinit var btnAddTopic: ImageButton
    lateinit var btn_return: ImageButton

    lateinit var topicCV: RecyclerView
    lateinit var topicRecordRVAdapter: TopicRecordRVAdapter
    var topicRecord = ArrayList<TopicRecordFormat>()

    private lateinit var auth: FirebaseAuth
    private lateinit var DB_Reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_topic)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        topicCV = findViewById(R.id.topicCV)
        topicCV.layoutManager= LinearLayoutManager(this)
        topicRecordRVAdapter = TopicRecordRVAdapter(topicRecord)
        topicCV.adapter = topicRecordRVAdapter

        btnAddTopic = findViewById(R.id.btnAddTopic)
        btn_return = findViewById(R.id.btn_return)

        auth = Firebase.auth
        DB_Reference = DB_Connection().connectRealDB()

        DB_Reference.child("Topic").child(auth.uid.toString()).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                addTopicRecordsFromFirebase(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        btn_return.setOnClickListener{
            finish()
        }

        btnAddTopic.setOnClickListener{
            startActivity(Intent(this, EditTopic::class.java))
        }
    }

    private fun addTopicRecordsFromFirebase(snapshot: DataSnapshot) {
        topicRecord.clear()
        Log.d("record", snapshot.value.toString())

        for (index in snapshot.children){
            Log.d("index", index.value.toString())
            var newRecord = TopicRecordFormat(index.key.toString(), index.child("TopicName").value.toString(), auth.uid.toString())

            topicRecord.add(newRecord)

            topicRecordRVAdapter.notifyDataSetChanged()
        }
    }
}

data class TopicRecordFormat(var topicID: String, var topicName: String, var authorUID: String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?: "",
        parcel.readString()?: "",
        parcel.readString()?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(topicID)
        parcel.writeString(topicName)
        parcel.writeString(authorUID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopicRecordFormat> {
        override fun createFromParcel(parcel: Parcel): TopicRecordFormat {
            return TopicRecordFormat(parcel)
        }

        override fun newArray(size: Int): Array<TopicRecordFormat?> {
            return arrayOfNulls(size)
        }
    }
}