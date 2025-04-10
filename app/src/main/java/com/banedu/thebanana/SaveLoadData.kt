package com.banedu.thebanana

import android.content.Context
import android.content.SharedPreferences

class SaveLoadData {

    val SHARED_PREFS: String = "sharedPrefs"

    var username: String = ""
    var email: String = ""
    var password: String = ""

    var role: String = ""

    var volume: Int = 0
    var music: Int = 0

    fun LoadData(activity: Context) {
        var sharedPreferences: SharedPreferences =
            activity.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        username = sharedPreferences.getString("username", "").toString()
        email = sharedPreferences.getString("email", "").toString()
        password = sharedPreferences.getString("password", "").toString()

        role = sharedPreferences.getString("role", "student").toString()

        volume = sharedPreferences.getInt("volume", 100)
        music = sharedPreferences.getInt("music", 100)
    }

    fun SaveData(activity: Context) {
        var sharedPreferences: SharedPreferences =
            activity.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putString("username", username)
        editor.putString("email", email)
        editor.putString("password", password)

        editor.putString("role", role)

        editor.putInt("volume", volume)
        editor.putInt("music", music)

        editor.apply()
    }
}