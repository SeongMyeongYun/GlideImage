package com.danchoo.glideimage

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.*
import kotlin.math.roundToInt

@Stable
class GlideImagePainter internal constructor(
    private val context: Context,
    private val loader: GlideImageLoader,
    private val parentScope: CoroutineScope,
    private val viewSize: Size,
    private val builder: RequestBuilder<Drawable>
) : Painter(), RememberObserver {
    internal var painter: Painter? by mutableStateOf(null)
    internal var placeHolder: Painter? by mutableStateOf(null)
    internal var drawable: Drawable? by mutableStateOf(null)

    private var job: Job? = null

    override val intrinsicSize: Size
        get() = painter?.intrinsicSize ?: Size.Unspecified

    private val target = object : CustomTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            drawable = resource

        }

        override fun onLoadCleared(placeholder: Drawable?) {
            painter = null
            placeHolder = null
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            drawable = errorDrawable
        }
    }

    override fun DrawScope.onDraw() {
        val size = if (intrinsicSize.isSpecified) {
            intrinsicSize
        } else {
            updateRequestSize(canvasSize = size)
        }


        placeHolder?.apply { draw(size) }
        painter?.apply { draw(size) }
    }

    private fun updateRequestSize(canvasSize: Size): Size {
        return Size(
            width = when {
                // If we have a canvas width, use it...
                canvasSize.width >= 0.5f -> canvasSize.width.roundToInt()
                // Otherwise we fall-back to the root view size as an upper bound
                viewSize.width > 0 -> viewSize.width
                else -> -1
            }.toFloat(),
            height = when {
                // If we have a canvas height, use it...
                canvasSize.height >= 0.5f -> canvasSize.height.roundToInt()
                // Otherwise we fall-back to the root view size as an upper bound
                viewSize.height > 0 -> viewSize.height
                else -> -1
            }.toFloat(),
        )
    }

    override fun onAbandoned() {
        cancel()
    }

    override fun onForgotten() {
        cancel()
    }

    override fun onRemembered() {
        val scope = CoroutineScope(parentScope.coroutineContext + SupervisorJob())
        job = scope.launch {
            builder.into(target)
        }
    }

    private fun cancel() {
        kotlin.runCatching {
            loader.getRequestManager(context).clear(target)
            job?.cancel()
            job = null
        }
    }
}

@Composable
fun rememberGlideImagePinter(
    data: Any?,
    size: Dp = 0.dp,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Fit,
    requestBuilder: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable> = { this }
) = rememberGlideImagePinter(
    data = data,
    width = size,
    height = size,
    placeHolder = placeHolder,
    contentScale = contentScale,
    requestBuilder = requestBuilder
)

@SuppressLint("UseCompatLoadingForDrawables", "CheckResult")
@Composable
fun rememberGlideImagePinter(
    data: Any?,
    width: Dp,
    height: Dp = width,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Fit,
    isAnimation: Boolean = false,
    requestBuilder: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable> = { this }
): Painter {
    val context = LocalContext.current
    val loader = LocalImageLoader.current
    var builder = loader.getRequestBuilder(context).load(data)

    builder = placeHolder?.let {
        builder.placeholder(it)
    } ?: builder


    val pxW = LocalDensity.current.run { width.toPx() }
    val pxH = LocalDensity.current.run { height.toPx() }
    if (width != 0.dp && height != 0.dp) {
        builder = builder.override(
            pxW.toInt(),
            pxH.toInt()
        )
    }

    builder = when (contentScale) {
        ContentScale.Crop -> builder.centerCrop()
        ContentScale.Inside -> builder.centerInside()
        else -> builder
    }

    if (isAnimation || isAnimatedResource(data)) {
        builder = builder.setAnimationListener()
    }

    builder = requestBuilder(builder)

    val scope = rememberCoroutineScope { Dispatchers.Main.immediate }
    val painter = remember(scope, data) {
        GlideImagePainter(
            context = context,
            builder = builder,
            loader = loader,
            viewSize = Size(pxW, pxH),
            parentScope = scope
        )
    }

    if (builder.placeholderId != 0) {
        context.getDrawable(builder.placeholderId)?.let {
            painter.placeHolder = rememberDrawablePainter(it)
        }
    }


    painter.drawable?.let {
        painter.painter = rememberDrawablePainter(drawable = painter.drawable)
    } ?: run {
        painter.painter = null
    }

    return painter
}

@Composable
private fun RequestBuilder<Drawable>.setAnimationListener(): RequestBuilder<Drawable> {
    val fitCenter = FitCenter()
    val loopCount = LocalImageLoader.current.getAnimationDrawableLoopCount()

    return this
        .optionalTransform(fitCenter)
        .optionalTransform(WebpDrawable::class.java, WebpDrawableTransformation(fitCenter))
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource:
                DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (resource is WebpDrawable) {
                    resource.loopCount = loopCount
                } else if (resource is GifDrawable) {
                    resource.setLoopCount(loopCount)
                }
                return false
            }
        })
}

private fun isAnimatedResource(data: Any?): Boolean {
    val url: String = data as? String ?: return false
    return url.split(".").last().lowercase().let {
        it == "webp" || it == "gif"
    }
}
