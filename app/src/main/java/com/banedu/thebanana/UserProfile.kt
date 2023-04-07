package com.banedu.thebanana

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserProfile : AppCompatActivity(), FilePicker.ImageUploadListener,
    FileRetriever.ImageDownloadListener {

    private lateinit var DB_Reference: DatabaseReference

    lateinit var SLD: SaveLoadData

    lateinit var edtUName: EditText
    lateinit var btn_logout: Button
    lateinit var btn_return: ImageButton
    lateinit var btn_profile_picture: ImageButton

    lateinit var adminProfile: LinearLayout
    lateinit var txtTotalTopic: TextView
    lateinit var txtTotalQuestion: TextView
    lateinit var btn_generate_code: Button

    lateinit var TableUserResult: TableLayout

    //TODO:TEST YO
    lateinit var userRecordRV: RecyclerView
    lateinit var userRecordRVAdapter: UserRecordRvAdapter
    lateinit var userRecord: ArrayList<UserRecordFormat>

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

        TableUserResult = findViewById(R.id.TableUserResult)

        adminProfile = findViewById(R.id.adminProfile)
        txtTotalTopic = findViewById(R.id.txtTotalTopic)
        txtTotalQuestion = findViewById(R.id.txtTotalQuestion)
        btn_generate_code = findViewById(R.id.btn_generate_code)

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

                SLD.role = "student"

                SLD.SaveData(this)

                finish()

                startActivity(Intent(this, LoginAccountFirebase::class.java))
                overridePendingTransition(0, 0) //Remove transition animation
            }
        }

        btn_return.setOnClickListener {
            finish()
        }

        btn_generate_code.setOnClickListener{
            var code = generateSixDigitCode()

            DB_Reference.child("Activation Code").get().addOnSuccessListener {
                var codeExist = true

                while (codeExist){
                    codeExist = false

                    if(it.exists()){
                        for(codeDB in it.children){
                            if(codeDB.value.toString() == code){
                                codeExist = true

                                code = generateSixDigitCode()

                                break
                            }
                        }
                    }
                }

                //code dont exist, can use
                DB_Reference.child("Activation Code").push().setValue(code)

                // Create a notification channel
                val channel = NotificationChannel(code, "Show code", NotificationManager.IMPORTANCE_DEFAULT)
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)

                // Create a notification
                val notification = NotificationCompat.Builder(this, code)
                    .setSmallIcon(R.drawable.banana)
                    .setContentTitle("Activation code")
                    .setContentText(code)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()

                // Show the notification
                notificationManager.notify(1, notification)
            }
        }

        //endregion

        //TODO:TEST TEST WKWKWKKWWK
        userRecordRV = findViewById(R.id.idRVRecord)
        userRecord = ArrayList()

        if(SLD.role == "student"){

            adminProfile.visibility = View.GONE

            userRecordRV.layoutManager= LinearLayoutManager(this)
            userRecordRVAdapter = UserRecordRvAdapter(userRecord)
            userRecordRV.adapter = userRecordRVAdapter

            DB_Reference.get().addOnSuccessListener {
                var index = 1

                for(result in it.child("Quiz Records").child(uid).children){
                    var resultIndex = index

                    var date = result.child("Qdate").value.toString()

                    var subjectTitle = ""

                    for(authorUID in it.child("Topic").children){
                        for(topicID in authorUID.children){
                            if (result.child("Subject").value.toString() == topicID.key.toString()){
                                subjectTitle = topicID.child("TopicName").value.toString()
                                break
                            }
                        }
                    }

                    var banana = result.child("Banana_Earned").value.toString().toInt()

                    index++
                    userRecord.add(UserRecordFormat(resultIndex, subjectTitle, date, banana))
                }

                userRecordRVAdapter.notifyDataSetChanged()
            }
        }else{
            TableUserResult.visibility = View.GONE;

            var totalQuestion = 0


            //check this admin statistic
            DB_Reference.get().addOnSuccessListener {
                txtTotalTopic.text = "Total topic: ${it.child("Topic").child(auth.uid.toString()).childrenCount}"

                for(topicID in it.child("Topic").child(auth.uid.toString()).children){
                    totalQuestion += topicID.childrenCount.toInt() - 1
                }

                txtTotalQuestion.text = "Total question created: $totalQuestion"

            }
        }
    }

    fun generateSixDigitCode(): String{
        val random = (0..999999).random()
        val formatted = String.format("%06d", random)

        return formatted
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

    override fun onImageDownloaded(uri: Uri?) {
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
