package com.example.cleanarchitectureproject.data.local

import androidx.room.TypeConverter
import com.example.cleanarchitectureproject.data.remote.dto.coinmarket.QuoteCM
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromQuoteCMList(value: List<QuoteCM>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toQuoteCMList(value: String): List<QuoteCM> {
        val type = object : TypeToken<List<QuoteCM>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }
}
