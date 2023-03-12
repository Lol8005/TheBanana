package com.example.stiventure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference

class LoginAccount : AppCompatActivity() {

    lateinit var SLD: SaveLoadData

    lateinit var edt_username: EditText
    lateinit var edt_password: EditText
    lateinit var btn_clear: Button
    lateinit var btn_login: Button
    lateinit var btn_go_register: Button

    lateinit var username: String
    lateinit var password: String

    private lateinit var DB_Reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_account)

        //region Hide toolbar

        var HSB: HideSystemBar = HideSystemBar()
        HSB.hide(window)

        //endregion

        //region Load Save Data

        SLD = SaveLoadData()
        SLD.LoadData(this)

        username = SLD.username
        password = SLD.password

        //endregion

        //region Initialize Variable

        edt_username = findViewById(R.id.edtUsernameL)
        edt_password = findViewById(R.id.edtPasswordL)
        btn_clear = findViewById(R.id.btn_clearL)
        btn_login = findViewById(R.id.btn_login)
        btn_go_register = findViewById(R.id.btn_go_register)

        DB_Reference = DB_Connection().connect()

        //endregion

        checkIfPlayerAlreadyLogin()

        btn_login.setOnClickListener{
            username = edt_username.text.toString()
            password = edt_password.text.toString()

            if(username == "" && password == ""){
                //empty
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_LONG).show()
            }

            if (checkIfTextEnterFulfillRequirement()){
                Login()
            }else{
                Toast.makeText(this, "Input not fulfill requirement!!", Toast.LENGTH_LONG).show()
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

        btn_go_register.setOnClickListener{
            finish()

            startActivity(Intent(this, RegisterAccount::class.java))
            overridePendingTransition(0, 0) //Remove transition animation
        }
    }

    private fun checkIfTextEnterFulfillRequirement(): Boolean{
        return username.length >= 6 && password.length >= 6 && !edt_username.text.contains(Regex("\\W"))
    }

    private fun checkIfPlayerAlreadyLogin(){
        if (username == "" || password == ""){
            return
        }

        Login()
    }

    private fun Login(){
        //if saved data not null
        //check if username and password valid
        DB_Reference.child("Users").child(username).child("password").get().addOnSuccessListener {
            val value = it.value

            //if value == null, account not exists
            //check password, if account exists
            if(it.value == password){
                //password correct
                Toast.makeText(this, "Login Success!!!", Toast.LENGTH_LONG).show()

                SLD.username = username
                SLD.password = password
                SLD.SaveData(this)

                finish()
            }else{
                Toast.makeText(this, "Username or password invalid", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error getting data $it", Toast.LENGTH_LONG).show()
        }
    }
}