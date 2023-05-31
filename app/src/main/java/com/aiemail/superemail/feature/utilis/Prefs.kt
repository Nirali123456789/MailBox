package com.aiemail.superemail.feature.utilis

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context)
{
    private var APP_PREF_INT_EXAMPLE = "Demo";
    private var IsLogin = "Islogin";

    private val preferences: SharedPreferences = context.getSharedPreferences(APP_PREF_INT_EXAMPLE,Context.MODE_PRIVATE)

    var intExamplePref: Boolean
        get() = preferences.getBoolean(APP_PREF_INT_EXAMPLE, false)
        set(value) = preferences.edit().putBoolean(APP_PREF_INT_EXAMPLE, value).apply()

    var islogin: Boolean
        get() = preferences.getBoolean(IsLogin, false)
        set(value) = preferences.edit().putBoolean(IsLogin, value).apply()
}