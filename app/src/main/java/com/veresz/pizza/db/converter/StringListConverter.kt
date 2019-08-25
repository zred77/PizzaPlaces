package com.veresz.pizza.db.converter

import androidx.room.TypeConverter
import com.veresz.pizza.api.jsonAdapter
import com.veresz.pizza.di.AppModule

class StringListConverter {

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        if (list == null) {
            return null
        }
        return AppModule.providesMoshi().jsonAdapter<List<String>>().toJson(list)
    }

    @TypeConverter
    fun toList(json: String?): List<String>? {
        if (json == null) {
            return null
        }
        return AppModule.providesMoshi().jsonAdapter<List<String>>().fromJson(json)
    }
}
