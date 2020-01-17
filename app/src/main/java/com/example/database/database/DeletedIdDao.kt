package com.example.database.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DeletedIdDao {
    @Query("SELECT * FROM deleted_id_table")
    fun getAll(): List<DeletedId>

    @Insert
    fun insert(id: DeletedId)
}