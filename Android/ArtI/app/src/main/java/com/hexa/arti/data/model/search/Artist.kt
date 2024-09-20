package com.hexa.arti.data.model.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    val artistId: Int,
    val korName: String,
    val engName: String,
    val imageUrl: String,
    val description: String? = null,
) : Parcelable
