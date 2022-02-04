package com.danchoo.glideimage

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.bumptech.glide.RequestBuilder

@Throws(IllegalArgumentException::class)
@Composable
fun GlideImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    data: Any?,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Fit,
    requestBuilder: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable> = { this }
) {

    val checkData = checkData(data)

    BoxWithConstraints(modifier = modifier) {
        GlideImage(
            modifier = Modifier,
            contentDescription = contentDescription,
            data = checkData,
            width = maxWidth,
            height = maxHeight,
            placeHolder = placeHolder,
            contentScale = contentScale,
            requestBuilder = requestBuilder
        )
    }
}

@Composable
fun GlideImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    data: Any?,
    width: Dp,
    height: Dp = width,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Fit,
    requestBuilder: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable> = { this }
) {
    Image(
        modifier = modifier,
        painter = rememberGlideImagePinter(
            data = data,
            width = width,
            height = height,
            placeHolder = placeHolder,
            contentScale = contentScale,
            requestBuilder = requestBuilder
        ),
        contentDescription = contentDescription
    )
}


@Throws(IllegalArgumentException::class)
private fun checkData(data: Any?): Any? {
    when (data) {
        is Drawable -> {
            throw IllegalArgumentException(
                "Unsupported type: Drawable." +
                        " If you wish to load a drawable, pass in the resource ID."
            )
        }
        is ImageBitmap -> {
            throw IllegalArgumentException(
                "Unsupported type: ImageBitmap." +
                        " If you wish to display this ImageBitmap, use androidx.compose.foundation.Image()"
            )
        }
        is ImageVector -> {
            throw IllegalArgumentException(
                "Unsupported type: ImageVector." +
                        " If you wish to display this ImageVector, use androidx.compose.foundation.Image()"
            )
        }
        is Painter -> {
            throw IllegalArgumentException(
                "Unsupported type: Painter." +
                        " If you wish to draw this Painter, use androidx.compose.foundation.Image()"
            )
        }
    }
    return data
}


