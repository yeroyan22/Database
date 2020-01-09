package com.example.database

import com.example.database.database.Article
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiInterface {

    @GET("/search?q=12%20years%20a%20slave&format=json&tag=film/film,tone/reviews&from-date=2010-01-01&show-tags=contributor&show-fields=starRating,headline,thumbnail,short-url&order-by=relevance&api-key=test")
    fun getArticles(): Call<List<Article>>

    companion object {

        private var BASE_URL = "http://open-platform.theguardian.com"

        fun create(): ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}