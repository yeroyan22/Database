package com.example.database.model

import com.example.database.database.Article
import com.google.gson.annotations.SerializedName

class Response(
    @SerializedName("results")
    var articles: List<Article>
) {
}