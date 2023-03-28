package com.banedu.thebanana

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.banedu.thebanana.FileRetriever
import com.banedu.thebanana.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
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
    var imgSet:ArrayList<Drawable> = ArrayList()
    var i=0
    var UserRanking = ArrayList<UserRankClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)

//        auth=Firebase.auth
//        val uid =auth.currentUser?.uid.toString()
        var myRefRank = Firebase.database.getReference("Users").child(uid)
        var RefRank = Firebase.database.getReference("Users")
        var RefUser = Firebase.database.getReference("Users")

        txtFplace = findViewById(R.id.txtFplace)
        txtSplace = findViewById(R.id.txtSplace)
        txtTplace = findViewById(R.id.txtTplace)
        txtYrRank = findViewById(R.id.txtYrRank)
        imgVwFirst = findViewById(R.id.imgVwFirst)
        imgVwSecond = findViewById(R.id.imgVwSecond)
        imgVwThird = findViewById(R.id.imgVwThird)
        btnBackHomeFromRank = findViewById(R.id.btnBackHomeFromRank)


        var max1 = 0
        var max2 = 0
        var max3 = 0
        var top1 = ""
        var top2 = ""
        var top3 = ""
        var top1uid = ""
        var top2uid = ""
        var top3uid = ""
        var temp1 = 0
        var tempName1 = ""
        var tempUid1 = ""
        var temp2 = 0
        var tempName2 = ""
        var tempUid2 = ""
        var ranking = 0
        var myBananaCount = 0
        var UserID: ArrayList<String> = ArrayList()

        btnBackHomeFromRank.setOnClickListener {
            finish()
        }

        //TODO: Get user banana count
        myRefRank.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dsnapshot: DataSnapshot) {
                if (dsnapshot.exists()) {
                    myBananaCount =
                        dsnapshot.child("Total_Banana_Earned").getValue().toString().toInt()
                } else {
                    Log.d("TAG", "dsnapshot not exists")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ERROR", "Failed to read value.", error.toException())
            }

        })

        //TODO: GET USER ID
        RefUser.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dsnapshot: DataSnapshot) {
                if (dsnapshot.exists()) {
                    for (y in dsnapshot.children) {
                        UserID.add(y.getKey().toString())
                    }
                } else {
                    Log.d("TAG", "dsnapshot1 not exists")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ERROR", "Failed to read value.", error.toException())
            }

        })

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
                    UserRanking = UserRanking.sortedByDescending { it.banana }.take(3) as ArrayList<UserRankClass>
                    Log.d("TAG", UserRanking.toString())

                    for (i in 0..2){
                        txtFplace.setText("${UserRanking[i].username} (${UserRanking[i].banana})")
                        fileRetriever = FileRetriever(UserRanking[i].id)
                        fileRetriever.loadImage(this@rank)
                    }
                }

                //region Hide
//                if (snapshot.exists()) {
//                    ranking = snapshot.childrenCount.toInt()
//                    var x = 0
//                    for (y in snapshot.children) {
//                        var value1 =
//                            snapshot.child(UserID[x]).child("Total_Banana_Earned").getValue()
//                                .toString().trim().toInt()
//                        var value2 =
//                            snapshot.child(UserID[x]).child("username").getValue().toString()
//                        Log.d("TAG", value1.toString())
//                        if (UserID[x]!=uid) {
//                            if ((value1) <= myBananaCount) {
//                                ranking--
//                            }
//                        }
//                        if ((value1) >= max1) {
//                            temp1 = max1
//                            tempName1 = top1
//                            tempUid1 = top1uid
//                            temp2 = max2
//                            tempName2 = top2
//                            tempUid2 = top2uid
//                            max1 = value1
//                            top1 = value2
//                            top1uid = UserID[x]
//                            max2 = temp1
//                            top2 = tempName1
//                            top2uid = tempUid1
//                            max3 = temp2
//                            top3 = tempName2
//                            top3uid = tempUid2
//                        } else if ((value1) >= max2) {
//                            temp2 = max2
//                            tempName2 = top2
//                            tempUid2 = top2uid
//                            max2 = value1
//                            top2 = value2
//                            top2uid = UserID[x]
//                            Log.d("TAG", top2uid)
//                            max3 = temp2
//                            top3 = tempName2
//                            top3uid = tempUid2
//                        } else if ((value1) >= max3) {
//                            max3 = value1
//                            top3 = value2
//                            top3uid = UserID[x]
//                        }
//                        x++
//                    }
//                    //TODO: Test test
//                    Log.d("TAG", "Top 1 is $top1 with banana amount: $max1 id:$top1uid")
//                    txtFplace.setText("$top1 ( $max1 )")
//                    fileRetriever = FileRetriever(top1uid)
//                    fileRetriever.loadImage(this@rank)
////                    imgVwFirst.setImageDrawable(imgSet[0])
//                    Log.d("TAG", "Top 2 is $top2 with banana amount: $max2 id:$top2uid")
//                    txtSplace.setText("$top2 ( $max2 )")
//                    fileRetriever = FileRetriever(top2uid)
//                    fileRetriever.loadImage(this@rank)
////                    imgVwSecond.setImageDrawable(imgSet[1])
//                    Log.d("TAG", "Top 3 is $top3 with banana amount: $max3 id:$top3uid")
//                    txtTplace.setText("$top3 ( $max3 )")
//                    fileRetriever = FileRetriever(top3uid)
//                    fileRetriever.loadImage(this@rank)
////                    imgVwThird.setImageDrawable(imgSet[2])
//                    Log.d("TAG", "Your Ranking is " + ranking)
//                    txtYrRank.setText("$ranking ($myBananaCount)")
//
//
//                } else {
//                    Log.d("TAG", "snapshot not exists")
//                }

                //endregion
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
//                    imgSet.add(resource)
//                    Log.d("Test", imgSet[x].toString())
                    val fileName = uri.path?.split("/")?.last().toString()

                    if(fileName == UserRanking[0].id){
                        imgVwFirst.setImageDrawable(resource)
                    }else if(fileName == UserRanking[1].id){
                        imgVwSecond.setImageDrawable(resource)
                    }else{
                        imgVwThird.setImageDrawable(resource)
                    }
                    i++
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle any cleanup required when the image is cleared
                }
            })

    }

}

data class UserRankClass(var id: String, var username: String, var banana: Int)