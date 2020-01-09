package com.example.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.database.database.Article
import com.example.database.database.ArticleDao
import com.example.database.database.ArticleDatabase


class ArticleViewModel(val database: ArticleDao, application: Application) :
    AndroidViewModel(application) {
    private val db: ArticleDatabase = ArticleDatabase.getInstance(application)

    fun insert(article: Article) {
        db.articleDao().insert(article)
    }

    fun update(article: Article) {
        db.articleDao().update(article)
    }

    fun delete(article: Article) {
        db.articleDao().delete(article)
    }

    fun getAllPosts(): LiveData<List<Article>> {
        return db.articleDao().findAll()
    }

    fun insertAllPosts(articleList: List<Article>) {
        return db.articleDao().insertAllArticles(articleList)
    }
}