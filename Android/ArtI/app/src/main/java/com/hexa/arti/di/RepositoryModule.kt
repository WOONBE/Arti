package com.hexa.arti.di

import com.hexa.arti.repository.TestRepository
import com.hexa.arti.repository.TestRepositoryImpl
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
    fun bindTestRepository(
        testRepositoryImpl: TestRepositoryImpl
    ): TestRepository
}