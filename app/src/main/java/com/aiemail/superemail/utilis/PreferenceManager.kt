package com.aiemail.superemail.utilis

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.aiemail.superemail.MyApplication


class PreferenceManager(context: Context) {
    lateinit var editor:SharedPreferences.Editor
    var _context:Context=context
    lateinit var pref: SharedPreferences
    fun SetUpPreference() {
         pref =
            _context.getSharedPreferences(
                _context.packageName + "" + "MyPref",
                0
            ) // 0 - for private mode

         editor = pref.edit()
    }

    public fun SetBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value);
        editor.commit()
    }

    public fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, false); // getting boolean
    }

    public fun SetString(key: String, value: String) {
        editor.putString(key, value);
        editor.commit()
    }

    public fun getString(key: String): String {
        return pref.getString(key, "")!!; // getting boolean
    }

    fun setInt(key: String, position: Int) {
        editor.putInt(key, position);
        editor.commit()

    }
    public fun getInt(key: String): Int {
        return pref.getInt(key, 0)!!; // getting boolean
    }
}