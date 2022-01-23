package com.danchoo.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import com.danchoo.glideimage.LocalImageLoader
import com.danchoo.sample.gallery.presentation.GalleryScreen
import com.danchoo.sample.ui.theme.GlideImageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GlideImageTheme {
                LocalDensity
                // A surface container using the 'background' color from the theme
                CompositionLocalProvider(LocalImageLoader provides GlideAppImageLoaderImpl()) {
                    GalleryScreen()
                }
            }
        }
    }
}
