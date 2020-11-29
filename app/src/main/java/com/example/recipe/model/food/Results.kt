package com.example.recipe.model.food


import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("sourceUrl")
    val sourceUrl: String,
    @SerializedName("imageUrls")
    val imageUrls: List<String>,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    @SerializedName("servings")
    val servings: Int,
    @SerializedName("title")
    val title: String
)