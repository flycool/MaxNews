package com.example.maxnews.data.local

import androidx.room.TypeConverter
import com.example.maxnews.data.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String = source.name

    @TypeConverter
    fun toSrouce(name: String): Source = Source(name, name)
}