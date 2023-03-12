package com.example.stiventure

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class RegisterAccount : AppCompatActivity() {

    lateinit var SLD: SaveLoadData

    lateinit var edt_username: EditText
    lateinit var edt_password: EditText

    lateinit var btn_clear: Button
    lateinit var btn_register: Button
    lateinit var btn_go_login: Button

    lateinit var username: String
    lateinit var password: String

    private lateinit var DB_Reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_account)

        //region Load Save Data

        SLD = SaveLoadData()
        SLD.LoadData(this)

        //endregion

        //region Initialize Variable

        DB_Reference = DB_Connection().connect()

        edt_username = findViewById(R.id.edtUsernameR)
        edt_password = findViewById(R.id.edtPasswordR)
        btn_register = findViewById(R.id.btn_register)
        btn_clear = findViewById(R.id.btn_clearR)
        btn_go_login = findViewById(R.id.btn_go_login)

        //endregion

        //region Hide toolbar

        var HSB: HideSystemBar = HideSystemBar()
        HSB.hide(window)

        //endregion

        btn_register.setOnClickListener{
            username = edt_username.text.toString()
            password = edt_password.text.toString()

            if(username == "" && password == ""){
                //empty
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_LONG).show()
            }else{
                //Not empty
                //check input fulfill requirement
                if(checkIfTextEnterFulfillRequirement()){
                    DB_Reference.child("Users").child(username).get().addOnSuccessListener {
                        val value = it.value
                        Log.d("Firebase", "Value is: $value")

                        if (value == null){
                            //username not exists, create one and login
                            DB_Reference.child("Users").child(username).child("password").setValue(password)

                            SLD.username = username
                            SLD.password = password
                            SLD.SaveData(this)

                            Toast.makeText(this, "Account Registered, User Login!!!", Toast.LENGTH_LONG).show()
                            finish()
                        }else{
                            Toast.makeText(this, "Account Exists!!!", Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Input not fulfill requirement!!", Toast.LENGTH_LONG).show()
                }
            }
        }

        btn_clear.setOnClickListener{
            if(edt_username.text.toString() == "" && edt_password.text.toString() == ""){
                Toast.makeText(this, "Nothing to clear!!!", Toast.LENGTH_LONG).show()
            }else{
                edt_username.setText("")
                edt_password.setText("")
            }
        }

        btn_go_login.setOnClickListener{
            finish()

            startActivity(Intent(this, LoginAccount::class.java))
            overridePendingTransition(0, 0) //Remove transition animation
        }
    }

    private fun checkIfTextEnterFulfillRequirement(): Boolean{
        return username.length >= 6 && password.length >= 6 && !edt_username.text.contains(Regex("\\W"))
    }
}