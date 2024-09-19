package com.hexa.arti.data.model.search

data class Artist(
    val artistId: Int,
    val korName: String,
    val engName: String,
    val imageUrl: String,
    val description: String?= null,
)
