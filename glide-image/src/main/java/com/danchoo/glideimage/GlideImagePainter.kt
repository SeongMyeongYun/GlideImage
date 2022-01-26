package com.danchoo.glideimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.*

@Stable
class GlideImagePainter internal constructor(
    private val context: Context,
    private val loader: GlideImageLoader,
    private val parentScope: CoroutineScope,
    private val builder: RequestBuilder<Bitmap>
) : Painter(), RememberObserver {
    internal var painter: Painter? by mutableStateOf(null)
    internal var placeHolder: Painter? by mutableStateOf(null)

    private var job: Job? = null

    override val intrinsicSize: Size
        get() = painter?.intrinsicSize ?: Size.Unspecified

    private val target = object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            painter = BitmapPainter(resource.asImageBitmap())
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            painter = null
            (placeholder as BitmapDrawable?)?.bitmap?.let {
                placeHolder = BitmapPainter(it.asImageBitmap())
            } ?: kotlin.run {
                placeHolder = null
            }
        }
    }

    override fun DrawScope.onDraw() {
        placeHolder?.apply { draw(intrinsicSize) }
        painter?.apply { draw(intrinsicSize) }
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
    requestBuilder: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap> = { this }
) = rememberGlideImagePinter(
    data = data,
    width = size,
    height = size,
    placeHolder = placeHolder,
    contentScale = contentScale,
    requestBuilder = requestBuilder
)

@Composable
fun rememberGlideImagePinter(
    data: Any?,
    width: Dp,
    height: Dp = width,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Fit,
    requestBuilder: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap> = { this }
): Painter {
    if (data is ImageVector) {
        return rememberVectorPainter(image = data)
    }

    val context = LocalContext.current
    val loader = LocalImageLoader.current
    var builder = loader.getRequestBuilder(context).load(data)

    builder = placeHolder?.let {
        builder.placeholder(it)
    } ?: builder


    if (width != 0.dp && height != 0.dp) {
        builder = builder.override(
            LocalDensity.current.run { width.toPx() }.toInt(),
            LocalDensity.current.run { height.toPx() }.toInt()
        )
    }

    builder = when (contentScale) {
        ContentScale.Crop -> builder.centerCrop()
        ContentScale.Inside -> builder.centerInside()
        else -> builder
    }

    builder = requestBuilder(builder)

    val scope = rememberCoroutineScope { Dispatchers.Main.immediate }
    val painter = remember(scope, data) {
        GlideImagePainter(
            context = context,
            builder = builder,
            loader = loader,
            parentScope = scope
        )
    }

    if (builder.placeholderId != 0) {
        painter.placeHolder = painterResource(builder.placeholderId)
    }

    return painter
}