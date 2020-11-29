package com.example.recipe.model.food


import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("baseUri")
    val baseUri: String,
    @SerializedName("expires")
    val expires: Long,
    @SerializedName("isStale")
    val isStale: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("processingTimeMs")
    val processingTimeMs: Int,
    @SerializedName("results")
    val results: List<Results>,
    @SerializedName("totalResults")
    val totalResults: Int
)