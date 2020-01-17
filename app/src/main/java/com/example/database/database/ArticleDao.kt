package com.example.database.database

import androidx.room.*
import androidx.lifecycle.LiveData


@Dao
interface ArticleDao {
    @Insert
    fun insert(article: Article)

    @Update
    fun update(article: Article)

    @Delete
    fun delete(article: Article)

    @Query("DELETE FROM article_table")
    fun deleteAll()

    @Query("SELECT * FROM article_table")
    fun findAll(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllArticles(articleList: List<Article>)

    @Query("SELECT * FROM article_table")
    fun getAll(): List<Article>

}