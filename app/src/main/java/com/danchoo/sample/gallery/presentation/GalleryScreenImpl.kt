package com.danchoo.sample.gallery.presentation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.paging.compose.LazyPagingItems
import com.danchoo.glideimage.GlideImage
import com.danchoo.sample.gallery.domain.model.GalleryItemModel

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