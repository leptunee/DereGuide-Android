package com.dereguide.android.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dereguide.android.data.model.EventReward
import com.dereguide.android.data.model.GachaGuarantee

class Converters {
    
    private val gson = Gson()
    
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<Int>>() {}.type
            gson.fromJson(value, listType)
        }
    }
    
    @TypeConverter
    fun fromStringIntMap(value: Map<String, Int>?): String? {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toStringIntMap(value: String?): Map<String, Int>? {
        return if (value == null) null else {
            val mapType = object : TypeToken<Map<String, Int>>() {}.type
            gson.fromJson(value, mapType)
        }
    }
    
    @TypeConverter
    fun fromStringDoubleMap(value: Map<String, Double>?): String? {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toStringDoubleMap(value: String?): Map<String, Double>? {
        return if (value == null) null else {
            val mapType = object : TypeToken<Map<String, Double>>() {}.type
            gson.fromJson(value, mapType)
        }
    }
    
    @TypeConverter
    fun fromEventRewardList(value: List<EventReward>?): String? {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toEventRewardList(value: String?): List<EventReward>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<EventReward>>() {}.type
            gson.fromJson(value, listType)
        }
    }
    
    @TypeConverter
    fun fromGachaGuaranteeList(value: List<GachaGuarantee>?): String? {
        return gson.toJson(value)
    }
    
    @TypeConverter
    fun toGachaGuaranteeList(value: String?): List<GachaGuarantee>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<GachaGuarantee>>() {}.type
            gson.fromJson(value, listType)
        }
    }
}
