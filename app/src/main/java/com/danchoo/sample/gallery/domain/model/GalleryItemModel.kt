package com.danchoo.sample.gallery.domain.model

import android.net.Uri

data class GalleryItemModel(
    val id: Int,
    val uri: Uri? = null,
    val name: String,
    val addedDate: String = "",
    val modifiedDate: String = "",
    val size: Long = 0,
    val createDate: Long = 0,
    val mediaType: Int = 0,
    val mineType: String = ""
) {
    fun isAnimation(): Boolean {
        return mineType.contains("webp") || mineType.contains("gif")
    }
}
