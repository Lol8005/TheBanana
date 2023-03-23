package com.banedu.thebanana

import android.content.Context
import android.content.SharedPreferences

class SaveLoadData {

    val SHARED_PREFS: String = "sharedPrefs"

    var username: String = ""
    var email: String = ""
    var password: String = ""

    var volume: Float = 0f
    var music: Float = 0f

    fun LoadData(activity: Context){
        var sharedPreferences:SharedPreferences = activity.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        username = sharedPreferences.getString("username", "").toString()
        email = sharedPreferences.getString("email", "").toString()
        password = sharedPreferences.getString("password", "").toString()

        volume = sharedPreferences.getFloat("volume", 1f)
        music = sharedPreferences.getFloat("music", 1f)
    }

    fun SaveData(activity: Context){
        var sharedPreferences:SharedPreferences = activity.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putString("username", username)
        editor.putString("email", email)
        editor.putString("password", password)

        editor.putFloat("volume", volume)
        editor.putFloat("music", music)

        editor.apply()
    }
}