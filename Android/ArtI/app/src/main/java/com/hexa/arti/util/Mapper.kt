package com.hexa.arti.util

import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.data.model.response.GetArtWorkResponse
import com.hexa.arti.data.model.response.GetArtistResponse
import com.hexa.arti.data.model.response.GetRandomGenreArtWorkResponse
import com.hexa.arti.data.model.search.Artist

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
    title = this.title,
    description = this.description,
    year = this.year
)

fun GetRandomGenreArtWorkResponse.asArtwork() = Artwork(
    artworkId = this.id,
    imageUrl = this.imageUrl,
    title = this.title,
    description = this.description,
    year = this.year
)