package com.danchoo.glideimage

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Stable
class GlideImagePainter internal constructor(
) : Painter(), RememberObserver {
    internal var painter: Painter? by mutableStateOf(null)
    internal var placeHolder: Painter? by mutableStateOf(null)

    var canceled: (() -> Unit)? = null

    var test: String = ""

    override val intrinsicSize: Size
        get() = painter?.intrinsicSize ?: Size.Unspecified

    override fun DrawScope.onDraw() {
        placeHolder?.apply { draw(intrinsicSize) }
        painter?.apply { draw(intrinsicSize) }
    }

    override fun onAbandoned() {
        canceled?.invoke()
    }

    override fun onForgotten() {
        canceled?.invoke()
    }

    override fun onRemembered() {
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
): GlideImagePainter {
    val context = LocalContext.current
    var builder = LocalImageLoader.current.getRequestBuilder(context).load(data)

    builder = placeHolder?.let {
        builder.placeholder(it)
    } ?: builder

    if (width != 0.dp && height != 0.dp) {
        val pxW = LocalDensity.current.run { width.toPx() }.toInt()
        val pxH = LocalDensity.current.run { height.toPx() }.toInt()
        builder = builder.override(pxW, pxH)
    }

    builder = when (contentScale) {
        ContentScale.Crop -> builder.centerCrop()
        ContentScale.Inside -> builder.centerInside()
        else -> builder
    }

    builder = requestBuilder(builder)

    val scope = rememberCoroutineScope { Dispatchers.Main.immediate }
    val painter = remember(scope) { GlideImagePainter() }

    if (data is ImageVector) {
        painter.painter = rememberVectorPainter(image = data)
    } else {
        UpdateGlidePainter(imagePainter = painter, builder = builder, scope = scope)
    }

    return painter
}

@Composable
private fun UpdateGlidePainter(
    imagePainter: GlideImagePainter,
    builder: RequestBuilder<Bitmap>,
    scope: CoroutineScope
) {
    if (builder.placeholderId != 0) {
        imagePainter.placeHolder = painterResource(builder.placeholderId)
    }

    imagePainter.test = UUID.randomUUID().toString()

    val target = object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            imagePainter.painter = BitmapPainter(resource.asImageBitmap())
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            imagePainter.painter = null
            (placeholder as BitmapDrawable?)?.bitmap?.let {
                imagePainter.placeHolder = BitmapPainter(it.asImageBitmap())
            } ?: kotlin.run {
                imagePainter.placeHolder = null
            }
        }
    }

    builder.into(target)

    val requestManager = LocalImageLoader
        .current
        .getRequestManager(LocalContext.current)

    imagePainter.canceled = {
        scope.launch {
            requestManager.clear(target)
        }
    }
}