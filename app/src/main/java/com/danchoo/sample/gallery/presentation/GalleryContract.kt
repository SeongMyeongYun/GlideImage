package com.danchoo.sample.gallery.presentation

import com.danchoo.sample.gallery.presentation.base.BaseIntent
import com.danchoo.sample.gallery.presentation.base.BaseSideEffect
import com.danchoo.sample.gallery.presentation.base.BaseViewState

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