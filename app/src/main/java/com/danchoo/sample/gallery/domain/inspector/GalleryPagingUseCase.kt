package com.danchoo.sample.gallery.domain.inspector

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.danchoo.sample.gallery.domain.model.GalleryItemModel
import com.danchoo.sample.gallery.domain.repository.GalleryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryPagingUseCase @Inject constructor(
    private val repository: GalleryRepository
) {
    companion object {
        const val PAGE_SIZE = 30
    }

    operator fun invoke(): Flow<PagingData<GalleryItemModel>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PAGE_SIZE
        )
    ) {
        repository.getGalleryPagingSource()
    }.flow
}