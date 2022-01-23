package com.danchoo.sample.gallery.domain.model

import android.net.Uri

data class GalleryItemModel(
    val id: Int,
    val uri: Uri? = null,
    val name: String,
    val addedDate: String,
    val modifiedDate: String,
    val size: Long,
    val createDate: Long,
    val mediaType: Int,
    val mineType: String
)
