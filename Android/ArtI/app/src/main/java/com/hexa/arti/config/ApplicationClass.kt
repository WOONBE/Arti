package com.hexa.arti.config

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application() {

    companion object{
        val REPRESENT_ARTWORKS = listOf("")
    }
}