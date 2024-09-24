package com.hexa.arti.di

import com.hexa.arti.repository.ArtWorkRepository
import com.hexa.arti.repository.ArtWorkRepositoryImpl
import com.hexa.arti.repository.ArtWorkUploadRepository
import com.hexa.arti.repository.ArtWorkUploadRepositoryImpl
import com.hexa.arti.repository.ArtistRepository
import com.hexa.arti.repository.ArtistRepositoryImpl
import com.hexa.arti.repository.LoginRepository
import com.hexa.arti.repository.LoginRepositoryImpl
import com.hexa.arti.repository.SignUpRepository
import com.hexa.arti.repository.SignUpRepositoryImpl
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
        artWorkRepositoryImpl: ArtWorkRepositoryImpl
    ): ArtWorkRepository

    @Singleton
    @Binds
    fun bindArtistRepository(
        artistRepositoryImpl: ArtistRepositoryImpl
    ): ArtistRepository

    @Singleton
    @Binds
    fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ) : LoginRepository

    @Singleton
    @Binds
    fun bindSignUpRepository(
        signUpRepositoryImpl: SignUpRepositoryImpl
    ) : SignUpRepository

    @Singleton
    @Binds
    fun bindArtWorkUploadRepository(
        artWorkUploadRepositoryImpl: ArtWorkUploadRepositoryImpl
    ) : ArtWorkUploadRepository
}