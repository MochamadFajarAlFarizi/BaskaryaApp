package com.example.baskaryaapp.ui.setting

import android.content.Context
import android.content.SharedPreferences

class Prefference (context: Context){
    private val PREF_NAME = "Setting.pref"
    private val sharedPref: SharedPreferences
    val editor : SharedPreferences.Editor

    init {
        sharedPref=context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor =sharedPref.edit()
    }
    fun put(key:String, value: Boolean){
        editor.putBoolean(key, value)
            .apply()
    }
    fun getBoolean(key: String): Boolean{
        return sharedPref.getBoolean(key,false)
    }
}