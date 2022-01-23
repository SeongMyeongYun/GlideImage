package com.danchoo.glideimage

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager

val LocalImageLoader = GlideImageLoaderCompositionLocal()

interface GlideImageLoader {
    fun getRequestBuilder(context: Context): RequestBuilder<Bitmap>
    fun getRequestManager(context: Context): RequestManager
}

internal class GlideImageLoaderImpl : GlideImageLoader {
    override fun getRequestBuilder(context: Context): RequestBuilder<Bitmap> {
        return Glide.with(context).asBitmap()
    }

    override fun getRequestManager(context: Context): RequestManager {
        return Glide.with(context)
    }
}

@JvmInline
value class GlideImageLoaderCompositionLocal internal constructor(
    private val delegate: ProvidableCompositionLocal<GlideImageLoader> = staticCompositionLocalOf { GlideImageLoaderImpl() }
) {
    val current: GlideImageLoader
        @Composable get() = delegate.current


    infix fun provides(value: GlideImageLoader) = delegate provides value
}

val GlideImageLoader.context
    @Composable get() = LocalContext.current