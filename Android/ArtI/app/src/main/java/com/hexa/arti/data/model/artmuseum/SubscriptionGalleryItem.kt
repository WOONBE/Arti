package com.hexa.arti.data.model.artmuseum

import com.google.gson.annotations.SerializedName

data class SubscriptionGalleryItem(
    val galleryId: Int,
    val galleryImage: String,
    @SerializedName("galleryDescription")
    val galleryDescription: String,
    val galleryName: String,
    val ownerName: String,
    val viewCount: Int
)