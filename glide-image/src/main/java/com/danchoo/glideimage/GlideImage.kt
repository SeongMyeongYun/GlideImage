package com.danchoo.glideimage

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.bumptech.glide.RequestBuilder


@Composable
fun GlideImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    data: Any?,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Fit,
    requestBuilder: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap> = { this }
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        GlideImage(
            modifier = modifier,
            contentDescription = contentDescription,
            data = data,
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
