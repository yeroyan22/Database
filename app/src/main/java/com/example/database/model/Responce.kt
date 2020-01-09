package com.example.database.model

import com.google.gson.annotations.SerializedName

class Responce(
    @SerializedName("results")
    var results: Results
) {
}