package com.example.stiventure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

class LoginAccountFirebase : AppCompatActivity() {

    lateinit var SLD: SaveLoadData

    private lateinit var auth: FirebaseAuth
    private lateinit var DB_Reference: DatabaseReference

    lateinit var edt_email: EditText
    lateinit var edt_password: EditText
    lateinit var btn_clear: Button
    lateinit var btn_login: Button
    lateinit var btn_go_register: Button

    lateinit var email: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_account_firebase)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        //region Load Save Data

        SLD = SaveLoadData()
        SLD.LoadData(this)

        email = SLD.email
        password = SLD.password

        //endregion

        //region Initialize Variable

        auth = Firebase.auth
        DB_Reference = DB_Connection().connect()

        edt_email = findViewById(R.id.edtEmailL)
        edt_password = findViewById(R.id.edtPasswordL)
        btn_clear = findViewById(R.id.btn_clearL)
        btn_login = findViewById(R.id.btn_login)
        btn_go_register = findViewById(R.id.btn_go_register)

        //endregion

        //region Button On Click Listener

        btn_clear.setOnClickListener(){
            email = edt_email.text.toString()
            password = edt_password.text.toString()

            if (email != "" || password != ""){
                edt_email.setText("")
                edt_password.setText("")
            }else{
                Toast.makeText(this, "Nothing to clear!!!", Toast.LENGTH_LONG).show()
            }
        }

        btn_login.setOnClickListener(){
            email = edt_email.text.toString()
            password = edt_password.text.toString()

            if(email != "" && password != ""){
                if(checkIfTextEnterFulfillRequirement()){
                    performLogin()
                }else{
                    Toast.makeText(this, "Input not fulfill requirement!!", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "Please enter your information!!", Toast.LENGTH_LONG).show()
            }
        }

        btn_go_register.setOnClickListener{
            startActivity(Intent(this, RegisterAccountFirebase::class.java))
            overridePendingTransition(0, 0) //Remove transition animation

            finish()
        }

        //endregion

        performLogin()
    }

    private fun performLogin(){
        if(auth.currentUser == null && email != "" && password != ""){
            // No user is signed in
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    SLD.email = email
                    SLD.password = password

                    val id = auth.currentUser?.uid.toString()
                    DB_Reference.child("Users").child(id).child("username").get().addOnSuccessListener{
                        SLD.username = it.value.toString()
                    }

                    SLD.SaveData(this)

                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }else if(auth.currentUser != null){
            finish()
        }
    }

    private fun checkIfTextEnterFulfillRequirement(): Boolean{
        return password.length >= 8
    }

    //disable back button from killing app
    override fun onBackPressed() {
        return
    }
}