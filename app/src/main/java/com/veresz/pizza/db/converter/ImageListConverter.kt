package com.veresz.pizza.db.converter

import androidx.room.TypeConverter
import com.veresz.pizza.api.jsonListAdapter
import com.veresz.pizza.di.AppModule
import com.veresz.pizza.model.Image

class ImageListConverter {

    @TypeConverter
    fun fromList(list: List<Image>?): String? {
        if (list == null) {
            return null
        }
        return AppModule.providesMoshi().jsonListAdapter<Image>().toJson(list)
    }

    @TypeConverter
    fun toList(json: String?): List<Image>? {
        if (json == null) {
            return null
        }
        return AppModule.providesMoshi().jsonListAdapter<Image>().fromJson(json)
    }
}
