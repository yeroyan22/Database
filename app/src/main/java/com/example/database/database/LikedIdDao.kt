package com.example.database.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikedIdDao {

    @Query("SELECT * FROM liked_id_table")
    fun getAll(): List<LikedId>

    @Insert
    fun insert(id: LikedId)

    @Delete
    fun delete(id: LikedId)
}