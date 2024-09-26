package com.hexa.arti.data.model.artmuseum

data class MyGalleryThemeItem(
    val id : Int,
    val title: String,
    val images: List<String> // 이미지 리소스 ID 목록
)