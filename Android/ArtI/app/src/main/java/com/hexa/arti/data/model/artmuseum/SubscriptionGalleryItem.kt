package com.hexa.arti.data.model.artmuseum

data class SubscriptionGalleryItem(
    val galleryDescription: String,
    val galleryId: Int,
    val galleryImage: String,
    val galleryName: String,
    val ownerName: String,
    val viewCount: Int
)