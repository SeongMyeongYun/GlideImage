package com.danchoo.sample.gallery.presentation.glide

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.*
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.danchoo.glideimage.GlideImageLoader


@GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {

        val bitmapPoolSizeBytes = 1024 * 1024 * 30
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSizeBytes.toLong()))

        val memoryCacheSizeBytes: Long = 1024 * 1024 * 30
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes))

        val diskCacheSizeBytes: Long = 1024 * 1024 * 2000
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, diskCacheSizeBytes))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
    }
}

class GlideAppImageLoaderImpl : GlideImageLoader {
    override fun getRequestBuilder(context: Context): RequestBuilder<Drawable> {
        return GlideApp.with(context).asDrawable()
    }

    override fun getRequestManager(context: Context): RequestManager {
        return GlideApp.with(context)
    }

    override fun getAnimationDrawableLoopCount(): Int {
        return 5
    }
}
