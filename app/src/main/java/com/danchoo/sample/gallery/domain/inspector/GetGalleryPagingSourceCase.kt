package com.danchoo.sample.gallery.domain.inspector

import com.danchoo.sample.gallery.domain.repository.GalleryRepository
import javax.inject.Inject

class GetGalleryPagingSourceCase @Inject constructor(
    private val repository: GalleryRepository
) {
    operator fun invoke() = repository.getGalleryPagingSource()
}