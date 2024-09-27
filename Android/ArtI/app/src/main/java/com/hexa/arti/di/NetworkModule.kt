package com.hexa.arti.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hexa.arti.BuildConfig
import com.hexa.arti.network.ArtWorkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()
    

    @Singleton
    @Provides
    @Named("arti")
    fun provideArtIRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL)  // EC2
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Singleton
    @Provides
    @Named("arti_fast")
    fun provideArtIFastRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.FAST_SERVER_URL)  // fastAPI
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


}
