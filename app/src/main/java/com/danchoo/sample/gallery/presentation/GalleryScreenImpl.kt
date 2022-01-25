package com.danchoo.sample.gallery.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
                modifier = modifier.padding(it),
                contentPadding = PaddingValues(2.dp)
            ) {
                items(pagingItems.itemCount) { index ->
                    GlideImage(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(maxWidth / 3)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                            ),
                        data = pagingItems[index]?.uri ?: "",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}