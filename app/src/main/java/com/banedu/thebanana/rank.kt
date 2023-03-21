package com.banedu.thebanana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class rank : AppCompatActivity() {
    //TODO: Complete thsi by relating with database
    //TODO: Construct GUI


    var myRefRank=Firebase.database.getReference().child("4")
    var RefRank=Firebase.database.getReference()
    var x=1
    var max1=0
    var max2=0
    var max3=0
    var top1=""
    var top2=""
    var top3=""
    var temp1=0
    var tempName1=""
    var temp2=0
    var tempName2=""
    var ranking=0
    var myBananaCount=0
    var limitRank=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)

        myRefRank.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dsnapshot: DataSnapshot) {
                if(dsnapshot.exists()) {
                    myBananaCount=dsnapshot.child("Age").getValue().toString().toInt()
                }
                else{
                    Log.d("TAG","dsnapshot not exists")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ERROR", "Failed to read value.", error.toException())
            }

        })


        RefRank.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                        ranking= snapshot.getChildrenCount().toInt()
                        limitRank=snapshot.getChildrenCount().toInt()
                        for(y in snapshot.children) {
                            var value1 = snapshot.child(x.toString()).child("Age").getValue().toString()
                            var value2 = snapshot.child(x.toString()).child("Name").getValue().toString()
                            Log.d("TAG", value1)
                            Log.d("TAG", value2)
                            if(x!=2){
                                if((value1.trim().toInt())<myBananaCount){
                                    ranking--
//                                }else{
//                                    if(ranking!=limitRank){
//                                        ranking++
//                                    }
//                                }
//                            }else{
//                                if(ranking!=limitRank){
//                                    ranking--
                                }
                            }
                            if ((value1.trim().toInt())>=max1){
                                temp1=max1
                                tempName1=top1
                                temp2=max2
                                tempName2=top2
                                max1=value1.trim().toInt()
                                top1=value2
                                max2=temp1
                                top2=tempName1
                                max3=temp2
                                top3=tempName2
                            }else if ((value1.trim().toInt())>=max2){
                                temp2=max2
                                tempName2=top2
                                max2=value1.trim().toInt()
                                top2=value2
                                max3=temp2
                                top3=tempName2
                            }else if ((value1.trim().toInt())>=max3){
                                max3=value1.trim().toInt()
                                top3=value2
                            }
                            x++
                        }
                    Log.d("TAG","Top 1 is "+top1+" with banana amount: "+max1.toString())
                    Log.d("TAG","Top 2 is "+top2+" with banana amount: "+max2.toString())
                    Log.d("TAG","Top 3 is "+top3+" with banana amount: "+max3.toString())
                    Log.d("TAG","Your Ranking is "+ranking)

                }
                else{
                    Log.d("TAG","snapshot not exists")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ERROR", "Failed to read value.", error.toException())
            }

        })



    }


}