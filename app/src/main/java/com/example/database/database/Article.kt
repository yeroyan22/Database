package com.example.database.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "article_table")
data class Article(
    @ColumnInfo(name = "article_title")
    @SerializedName("webTitle")
    var title: String,
    @ColumnInfo(name = "article_image")
    @SerializedName("thumbnail")
    var image: String,
    @ColumnInfo(name = "article_category")
    @SerializedName("sectionName")
    var category: String
) {
}