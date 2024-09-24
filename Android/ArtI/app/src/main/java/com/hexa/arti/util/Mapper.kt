package com.hexa.arti.util

import com.hexa.arti.data.model.response.GetArtWorkResponse
import com.hexa.arti.data.model.response.GetArtistResponse
import com.hexa.arti.data.model.search.Artist
import com.hexa.arti.data.model.artwork.Artwork

fun GetArtistResponse.asArtist() = Artist(
    artistId = this.artistId,
    korName = this.korName,
    engName = this.engName,
    imageUrl = this.image,
    description = this.summary
)

fun GetArtWorkResponse.asArtwork() = Artwork(
    artworkId = this.artworkId,
    imageUrl = this.filename,
    title = this.title
)