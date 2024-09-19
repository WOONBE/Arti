package com.hexa.arti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetArtWorkResponse(
    val artist: String,
    val artistKo: String,
    @SerializedName("artwork_id")
    val artworkId: Int,
    val description: String,
    val filename: String,
    val genre: String,
    val genreCount: Int,
    val height: Int,
    val phash: String,
    val subset: String,
    val title: String,
    val width: Int,
    val year: String
)