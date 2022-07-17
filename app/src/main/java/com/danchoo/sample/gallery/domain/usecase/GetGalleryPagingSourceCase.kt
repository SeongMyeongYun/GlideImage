package com.danchoo.sample.gallery.domain.usecase

import com.danchoo.sample.gallery.domain.repository.GalleryRepository

class GetGalleryPagingSourceCase(
    private val repository: GalleryRepository
) {
    operator fun invoke() = repository.getGalleryPagingSource()
}