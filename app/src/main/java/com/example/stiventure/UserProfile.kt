package com.example.stiventure

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class UserProfile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var DB_Reference: DatabaseReference

    lateinit var SLD:SaveLoadData

    lateinit var txtUName: TextView
    lateinit var btn_logout: Button
    lateinit var btn_return: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //region Hide toolbar

        var HSB: HideSystemBar = HideSystemBar()
        HSB.hide(window)

        //endregion

        //region Load Save Data

        SLD = SaveLoadData()
        SLD.LoadData(this)

        //endregion

        //region Initialize Variable

        auth = Firebase.auth
        DB_Reference = DB_Connection().connect()

        txtUName = findViewById(R.id.txtUName)
        btn_logout = findViewById(R.id.btn_logout)
        btn_return = findViewById(R.id.btn_return)

        //endregion

        //region Button On Click Listener

        if(SLD.username == ""){
            val id = auth.currentUser?.uid.toString()
            DB_Reference.child("Users").child(id).child("username").get().addOnSuccessListener{
                SLD.username = it.value.toString()
                SaveLoadData()

                txtUName.setText("Hi, ${SLD.username}")
            }
        }

        btn_logout.setOnClickListener{
            if(auth.currentUser != null){
                auth.signOut()

                SLD.username = ""
                SLD.email = ""
                SLD.password = ""
                SLD.SaveData(this)

                finish()

                startActivity(Intent(this, LoginAccountFirebase::class.java))
                overridePendingTransition(0, 0) //Remove transition animation
            }
        }

        btn_return.setOnClickListener{
            finish()
        }

        //endregion
    }
}