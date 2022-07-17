package com.danchoo.sample.gallery.domain.di

import com.danchoo.sample.gallery.domain.repository.GalleryRepository
import com.danchoo.sample.gallery.domain.usecase.GetGalleryPagingSourceCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GalleryUseCaseModule {

    @Provides
    fun provideGalleryPagingUseCase(
        repository: GalleryRepository
    ): GetGalleryPagingSourceCase {
        return GetGalleryPagingSourceCase(repository)
    }
}