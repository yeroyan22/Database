package com.example.database.model

import com.example.database.database.Article
import com.google.gson.annotations.SerializedName


abstract class ArticlesData(
    @SerializedName("response")
    var response: Responce
) : List<Article> {
}