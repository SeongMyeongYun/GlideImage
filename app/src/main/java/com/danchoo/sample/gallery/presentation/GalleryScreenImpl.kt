package com.danchoo.sample.gallery.presentation

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.danchoo.glideimage.GlideImage
import com.danchoo.sample.gallery.domain.model.GalleryItemModel
import com.danchoo.sample.gallery.presentation.GalleryContract.GalleryViewState

@Composable
fun GalleryScreenImpl(
    modifier: Modifier,
    pagingItems: LazyPagingItems<GalleryItemModel>
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                modifier = modifier,
                navigationIcon = {
                },
                title = {
                })
        },
        snackbarHost = {}
    ) {

        BoxWithConstraints(modifier = modifier.fillMaxSize()) {
            val pxW = LocalDensity.current.run { maxWidth.toPx() }.toInt()
            Log.d("_SMY", "screen size BoxWithConstraints ${pxW}")

            LazyVerticalGrid(
                cells = GridCells.Fixed(3),
                modifier = modifier.padding(it)
            ) {
                items(pagingItems.itemCount) { index ->
                    GlideImage(
                        modifier = Modifier.size(maxWidth / 3),
                        data = pagingItems[index]?.uri,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}