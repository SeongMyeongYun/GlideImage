package com.danchoo.sample.gallery.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.danchoo.sample.gallery.domain.model.GalleryItemModel
import com.danchoo.sample.gallery.domain.usecase.GetGalleryPagingSourceCase
import com.danchoo.sample.gallery.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getPagingUseCase: GetGalleryPagingSourceCase,
) : BaseViewModel<GalleryContract.GalleryIntent, GalleryContract.GalleryViewState, GalleryContract.GallerySideEffect>() {

    fun galleryPagingItems(): Flow<PagingData<GalleryItemModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE
            )
        ) {
            getPagingUseCase()
        }.flow.cachedIn(viewModelScope)
    }

    override fun setInitialState() = GalleryContract.GalleryViewState()

    override fun handleEvents(event: GalleryContract.GalleryIntent) {
    }

    companion object {
        const val PAGE_SIZE = 30
    }
}
