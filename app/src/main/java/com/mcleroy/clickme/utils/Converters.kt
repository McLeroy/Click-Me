package com.mcleroy.clickme.utils

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun numberToString(value: Number): String {
        return value.toString()
    }

    @TypeConverter
    fun stringToNumber(value: String): Number {
        return value.toInt()
    }
}