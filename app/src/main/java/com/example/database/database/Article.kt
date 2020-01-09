package com.example.database.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "article_table")
data class Article(
    @PrimaryKey
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