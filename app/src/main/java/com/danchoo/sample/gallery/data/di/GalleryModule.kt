package com.danchoo.sample.gallery.data.di

import com.danchoo.sample.gallery.data.datasource.GalleryDataSource
import com.danchoo.sample.gallery.data.datasource.GalleryDataSourceImpl
import com.danchoo.sample.gallery.data.repository.GalleryRepositoryImpl
import com.danchoo.sample.gallery.domain.repository.GalleryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GalleryModule {

    @Binds
    abstract fun bindsGalleryRepository(
        repository: GalleryRepositoryImpl
    ): GalleryRepository

    @Binds
    abstract fun bindsGalleryDataSource(
        dataSource: GalleryDataSourceImpl
    ): GalleryDataSource

}