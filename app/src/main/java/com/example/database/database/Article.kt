package com.example.database.database

import androidx.room.*
import com.google.gson.annotations.SerializedName


@Entity(tableName = "article_table")
data class Article(
    @PrimaryKey
    @ColumnInfo(name = "article_id")
    @SerializedName("id")
    var id: String,
    @ColumnInfo(name = "article_title")
    @SerializedName("webTitle")
    var title: String,
    @ColumnInfo(name = "article_category")
    @SerializedName("sectionName")
    var category: String,
    @ColumnInfo(name = "article_url")
    @SerializedName("webUrl")
    var url: String
) {

    @SerializedName("fields")
    @TypeConverters(FieldsConverter::class)
    var fields: Fields? = null

    class Fields(
        @SerializedName("thumbnail")
        var image: String
    )

    class FieldsConverter {
        @TypeConverter
        fun toFields(image: String?): Fields? {
            return if (image == null) null else Fields(image)
        }

        @TypeConverter
        fun toImage(fields: Fields?): String? {
            return (fields?.image)
        }
    }

}