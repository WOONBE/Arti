package com.hexa.arti.util

import com.hexa.arti.data.model.response.GetArtistResponse
import com.hexa.arti.data.model.search.Artist

fun GetArtistResponse.asArtist() = Artist(
    artistId = this.artistId,
    korName = this.korName,
    engName = this.engName,
    imageUrl = this.image,
    description = this.summary
)