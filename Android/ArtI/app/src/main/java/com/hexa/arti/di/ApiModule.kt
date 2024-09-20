package com.hexa.arti.di

import com.hexa.arti.network.ArtWorkApi
import com.hexa.arti.network.ArtistApi
import com.hexa.arti.network.LoginApi
import com.hexa.arti.network.SignUpApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideArtiApiService(@Named("arti") retrofit: Retrofit): ArtWorkApi =
        retrofit.create(ArtWorkApi::class.java)

    @Singleton
    @Provides
    fun provideArtistApiService(@Named("arti") retrofit: Retrofit): ArtistApi =
        retrofit.create(ArtistApi::class.java)

    @Singleton
    @Provides
    fun provideLoginApiService(@Named("arti") retrofit: Retrofit): LoginApi =
        retrofit.create(LoginApi::class.java)

    @Singleton
    @Provides
    fun provideSignApiService(@Named("arti") retrofit: Retrofit): SignUpApi =
        retrofit.create(SignUpApi::class.java)
}