package com.example.database

import com.example.database.database.Article
import com.example.database.model.ArticlesData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("/search?q=12%20years%20a%20slave&format=json&tag=film/film,tone/reviews&from-date=2010-01-01&show-tags=contributor&show-fields=starRating,headline,thumbnail,short-url&order-by=relevance&api-key=da3e84a8-a10c-403d-9817-863e081ee713&page-size=50")
    fun getArticles(@Query("page") page: Int): Call<ArticlesData>

    companion object {

        private var BASE_URL = "https://content.guardianapis.com"

        fun create(): ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}