package com.aiemail.superemail.TypeConverters

import androidx.room.TypeConverter
import com.aiemail.superemail.Models.AllMails
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MapConverter {

    @TypeConverter
    fun fromString(value: String): Set<AllMails> {
        val listType = object : TypeToken<Set<AllMails>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<AllMails>): String {
        return Gson().toJson(list)
    }

}