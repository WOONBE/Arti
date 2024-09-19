package com.hexa.arti.data.model.response

import com.google.gson.annotations.SerializedName

data class GetArtistByStringResponse(
    @SerializedName("artist_id")
    val artistId: Int,
    val engName: String,
    val korName: String,
    val image: String,
    val summary: String,
)