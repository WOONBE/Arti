package com.hexa.arti.di

import com.hexa.arti.repository.ArtWorkRepository
import com.hexa.arti.repository.ArtWorkRepositoryIml
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindArtWorkRepository(
        artWorkRepositoryIml: ArtWorkRepositoryIml
    ): ArtWorkRepository
}