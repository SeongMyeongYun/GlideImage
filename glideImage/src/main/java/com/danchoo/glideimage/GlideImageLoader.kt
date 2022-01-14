package com.danchoo.glideimage

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

val LocalImageLoader = GlideImageLoaderCompositionLocal()

interface GlideImageLoader {
    fun getRequestBuilder(context: Context): RequestBuilder<Bitmap>
}

internal class GlideImageLoaderImpl : GlideImageLoader {
    override fun getRequestBuilder(context: Context): RequestBuilder<Bitmap> {
        return Glide.with(context).asBitmap()
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
