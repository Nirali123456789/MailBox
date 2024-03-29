package com.aiemail.superemail.TypeConverters

import androidx.room.TypeConverter

class StringArrayConverter {
    @TypeConverter
    fun fromStringArray(value: Array<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringArray(value: String): Array<String> {
        return value.split(",").toTypedArray()
    }


}