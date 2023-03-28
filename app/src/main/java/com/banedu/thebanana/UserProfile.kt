package com.banedu.thebanana

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class UserProfile : AppCompatActivity(), FilePicker.ImageUploadListener,
    FileRetriever.ImageDownloadListener {

    private lateinit var DB_Reference: DatabaseReference

    lateinit var SLD: SaveLoadData

    lateinit var edtUName: EditText
    lateinit var btn_logout: Button
    lateinit var btn_return: ImageButton
    lateinit var btn_profile_picture: ImageButton

    private lateinit var filePicker: FilePicker
    private var auth: FirebaseAuth=Firebase.auth
    val uid =auth.currentUser?.uid.toString()
    private val fileRetriever = FileRetriever(uid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        filePicker = FilePicker(this)
        fileRetriever.loadImage(this)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        //region Load Save Data

        SLD = SaveLoadData()
        SLD.LoadData(this)

        //endregion

        //region Initialize Variable

        DB_Reference = DB_Connection().connectRealDB()

        edtUName = findViewById(R.id.edtUName)
        btn_logout = findViewById(R.id.btn_logout)
        btn_return = findViewById(R.id.btn_return)
        btn_profile_picture = findViewById(R.id.btn_profile_picture)

        //endregion

        //region Button On Click Listener

        if (SLD.username == "") {
            DB_Reference.child("Users").child(uid).child("username").get().addOnSuccessListener {
                SLD.username = it.value.toString()

                edtUName.setText("${SLD.username}")
                SLD.SaveData(this)
            }
        } else {
            edtUName.setText("${SLD.username}")
        }

        edtUName.setOnFocusChangeListener { view, b ->
            //User done changing name
            if(edtUName.hasFocus() == false){
                var username = edtUName.text.toString()
                if(username != ""){
                    if(username.length >= 6){
                        resources.openRawResource(R.raw.badwords).bufferedReader().use {
                            var illegalWord = false

                            for(word in it.readText().split("\n")){
                                if(username.contains(word.trim())){
                                    illegalWord = true
                                    break
                                }
                            }

                            if(illegalWord){
                                Toast.makeText(this, "Please enter appropriate username!!", Toast.LENGTH_LONG).show()
                            }else{
                                DB_Reference.child("Users").child(auth.currentUser?.uid.toString()).child("username").setValue(username)
                                Toast.makeText(this, "Successfully change your username!!", Toast.LENGTH_LONG).show()
                                SLD.username = username
                                SLD.SaveData(this)
                            }
                        }
                    }else{
                        Toast.makeText(this, "Input not fulfill requirement!!", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Please enter your username!!", Toast.LENGTH_LONG).show()
                }
            }
        }

        btn_profile_picture.setOnClickListener{
            filePicker.pickFile(this)
        }

        btn_logout.setOnClickListener {
            if (auth.currentUser != null) {
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

        btn_return.setOnClickListener {
            finish()
        }

        //endregion
    }

    override fun onImageUploaded(uri: Uri) {
        // Do something with the downloaded URI
        Log.d("URI", uri.toString())

        Glide.with(this)
            .load(uri)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    // Set the Drawable to an ImageView or any other view that accepts a Drawable
                    btn_profile_picture.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle any cleanup required when the image is cleared
                }
            })
    }

    override fun onImageDownloaded(uri: Uri) {
        // Do something with the downloaded URI
        Log.d("URI", uri.toString())

        Glide.with(this)
            .load(uri)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    // Set the Drawable to an ImageView or any other view that accepts a Drawable
                    btn_profile_picture.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle any cleanup required when the image is cleared
                }
            })
    }
}
