package com.banedu.thebanana

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class rank : AppCompatActivity(), FileRetriever.ImageDownloadListener {
    lateinit var txtFplace: TextView
    lateinit var txtSplace: TextView
    lateinit var txtTplace: TextView
    lateinit var txtYrRank: TextView
    lateinit var imgVwFirst: ImageView
    lateinit var imgVwSecond: ImageView
    lateinit var imgVwThird: ImageView
    lateinit var btnBackHomeFromRank: ImageButton
    private var auth: FirebaseAuth = Firebase.auth
    val uid = auth.currentUser?.uid.toString()
    lateinit var fileRetriever: FileRetriever
    var UserRanking = ArrayList<UserRankClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)

//        auth=Firebase.auth
//        val uid =auth.currentUser?.uid.toString()
        var RefRank = Firebase.database.getReference("Users")

        txtFplace = findViewById(R.id.txtFplace)
        txtSplace = findViewById(R.id.txtSplace)
        txtTplace = findViewById(R.id.txtTplace)
        txtYrRank = findViewById(R.id.txtYrRank)
        imgVwFirst = findViewById(R.id.imgVwFirst)
        imgVwSecond = findViewById(R.id.imgVwSecond)
        imgVwThird = findViewById(R.id.imgVwThird)
        btnBackHomeFromRank = findViewById(R.id.btnBackHomeFromRank)

        var UserID: ArrayList<String> = ArrayList()

        btnBackHomeFromRank.setOnClickListener {
            finish()
        }

        //TODO: FIND RANK
        RefRank.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    for (indexID in snapshot.children){
                        var thisUserBanana = indexID.child("Total_Banana_Earned").value.toString().toInt()
                        var thisUsername = indexID.child("username").value.toString()

                        UserRanking.add(UserRankClass(indexID.key.toString(), thisUsername, thisUserBanana))
                        Log.d("Banana", UserRanking.toString())
                    }

                    // Sort and get top 3
                    var AllUserRanking = UserRanking.sortedByDescending { it.banana }
                    UserRanking = AllUserRanking.take(3) as ArrayList<UserRankClass>
                    Log.d("TAG", UserRanking.toString())

                    for (i in 0..2){
                        fileRetriever = FileRetriever(UserRanking[i].id)
                        fileRetriever.loadImage(this@rank)
                    }

                    txtFplace.setText("${UserRanking[0].username} (${UserRanking[0].banana})")
                    txtSplace.setText("${UserRanking[1].username} (${UserRanking[1].banana})")
                    txtTplace.setText("${UserRanking[2].username} (${UserRanking[2].banana})")


                    for(userIndex in 0..AllUserRanking.size){
                        if(uid == AllUserRanking[userIndex].id){
                            txtYrRank.setText("${userIndex + 1} (${AllUserRanking[userIndex].banana})")

                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ERROR", "Failed to read value.", error.toException())
            }
        })
    }

    override fun onImageDownloaded(uri: Uri) {
        // Do something with the downloaded URI
        Log.d("URI", uri.toString())
        Glide.with(this)
            .load(uri)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?
                ) {
                    // Set the Drawable to an ImageView or any other view that accepts a Drawable
                    val fileName = uri.path?.split("/")?.last().toString()

                    if(fileName == UserRanking[0].id){
                        imgVwFirst.setImageDrawable(resource)
                    }else if(fileName == UserRanking[1].id){
                        imgVwSecond.setImageDrawable(resource)
                    }else{
                        imgVwThird.setImageDrawable(resource)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle any cleanup required when the image is cleared
                }
            })

    }

}

data class UserRankClass(var id: String, var username: String, var banana: Int)