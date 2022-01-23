package com.danchoo.sample.gallery.presentation

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val permissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    LaunchedEffect(key1 = permissionState) {
        if (!permissionState.hasPermission) {
            permissionState.launchPermissionRequest()
        }
    }

    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {  },
        permissionNotAvailableContent = { }
    ) {
        val pagingItems = viewModel.galleryPagingItems().collectAsLazyPagingItems()

        GalleryScreenImpl(
            modifier = modifier,
            pagingItems = pagingItems
        )
    }
}