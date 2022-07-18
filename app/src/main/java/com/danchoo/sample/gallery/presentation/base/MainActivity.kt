package com.danchoo.sample.gallery.presentation.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.danchoo.glideimage.LocalImageLoader
import com.danchoo.sample.gallery.presentation.GalleryScreen
import com.danchoo.sample.gallery.presentation.glide.GlideAppImageLoaderImpl
import com.danchoo.sample.ui.theme.GlideImageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GlideImageTheme {
                // A surface container using the 'background' color from the theme
                CompositionLocalProvider(LocalImageLoader provides GlideAppImageLoaderImpl()) {
                    GalleryScreen()
                }
            }
        }
    }
}
