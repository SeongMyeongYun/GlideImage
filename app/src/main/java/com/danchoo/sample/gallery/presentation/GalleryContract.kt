package com.danchoo.sample.gallery.presentation

import com.danchoo.sample.BaseIntent
import com.danchoo.sample.BaseSideEffect
import com.danchoo.sample.BaseViewState
import com.danchoo.sample.gallery.domain.model.GalleryItemModel

object GalleryContract {

    sealed class GalleryIntent : BaseIntent {
        object Idle : GalleryIntent()
    }

    sealed class GallerySideEffect : BaseSideEffect {
        object Idle : GallerySideEffect()
    }

    data class GalleryViewState(
        val isCreate: Boolean = true
    ) : BaseViewState
}