package com.danchoo.glideimage

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.RequestBuilder


@Composable
fun GlideImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    data: Any?,
    size: Dp = 0.dp,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Fit,
    requestBuilder: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap> = { this }
) {
    GlideImage(
        modifier = modifier,
        contentDescription = contentDescription,
        data = data,
        width = size,
        height = size,
        placeHolder = placeHolder,
        contentScale = contentScale,
        requestBuilder = requestBuilder
    )
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
    requestBuilder: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap> = { this }
) {
    if (data is ImageVector) {
        Image(
            modifier = modifier,
            painter = rememberVectorPainter(image = data),
            contentDescription = contentDescription,
            contentScale = contentScale
        )
    } else {
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
}
