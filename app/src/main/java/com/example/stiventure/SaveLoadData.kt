package com.example.stiventure

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SaveLoadData {

    val SHARED_PREFS: String = "sharedPrefs"

    var volume: Int = 0
    var music: Int = 0

    fun LoadData(activity: Context){
        var sharedPreferences:SharedPreferences = activity.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        volume = sharedPreferences.getInt("volume", 100)
        music = sharedPreferences.getInt("music", 100)


    }

    fun SaveData(activity: Context){
        var sharedPreferences:SharedPreferences = activity.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putInt("volume", volume)
        editor.putInt("music", music)

        editor.apply()
    }
}