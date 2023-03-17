package com.banedu.thebanana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class RegisterAccountFirebase : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var DB_Reference: DatabaseReference

    lateinit var SLD: SaveLoadData

    lateinit var edt_username: EditText
    lateinit var edt_email: EditText
    lateinit var edt_password: EditText
    lateinit var btn_clear: Button
    lateinit var btn_register: Button
    lateinit var btn_go_login: Button

    lateinit var username: String
    lateinit var email: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_account_firebase)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        //region Initialize Variable

        auth = Firebase.auth
        DB_Reference = DB_Connection().connect()

        edt_username = findViewById(R.id.edtUsername)
        edt_email = findViewById(R.id.edtEmailL)
        edt_password = findViewById(R.id.edtPasswordL)
        btn_clear = findViewById(R.id.btn_clearL)
        btn_register = findViewById(R.id.btn_login)
        btn_go_login = findViewById(R.id.btn_go_register)

        //endregion

        //region Load Save Data

        SLD = SaveLoadData()
        SLD.LoadData(this)

        email = SLD.email
        password = SLD.password

        //endregion

        //region Button On Click Listener

        btn_clear.setOnClickListener(){
            username = edt_username.text.toString()
            email = edt_email.text.toString()
            password = edt_password.text.toString()

            if (username != "" || email != "" || password != ""){
                edt_username.setText("")
                edt_email.setText("")
                edt_password.setText("")
            }else{
                Toast.makeText(this, "Nothing to clear!!!", Toast.LENGTH_LONG).show()
            }
        }

        btn_register.setOnClickListener(){

            username = edt_username.text.toString()
            email = edt_email.text.toString()
            password = edt_password.text.toString()

            if(username != "" && email != "" && password != ""){
                if(checkIfTextEnterFulfillRequirement()){
                    performSignUp()
                }else{
                    Toast.makeText(this, "Input not fulfill requirement!!", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "Please enter your information!!", Toast.LENGTH_LONG).show()
            }
        }

        btn_go_login.setOnClickListener{
            startActivity(Intent(this, LoginAccountFirebase::class.java))
            overridePendingTransition(0, 0) //Remove transition animation

            finish()
        }

        //endregion
    }

    private fun performSignUp(){
        username = edt_username.text.toString()
        email = edt_email.text.toString()
        password = edt_password.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful) {

                val id = auth.currentUser?.uid.toString()
                DB_Reference.child("Users").child(id).child("username").setValue(username)

                SLD.username = username
                SLD.email = email
                SLD.password = password

                SLD.SaveData(this)

                finish()
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfTextEnterFulfillRequirement(): Boolean{
        return username.length >= 6 && password.length >= 8 && !edt_username.text.contains(Regex("\\W"))
    }

    //disable back button from killing app
    override fun onBackPressed() {
        return
    }
}