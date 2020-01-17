package com.example.database.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "liked_id_table")
data class LikedId(
    @PrimaryKey
    @ColumnInfo(name = "article_id")
    var id: String
) {
}