package com.danchoo.sample

import android.app.Activity
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import coil.compose.rememberImagePainter
import com.danchoo.glideimage.GlideImage
import com.danchoo.glideimage.LocalImageLoader
import com.danchoo.sample.gallery.presentation.GalleryScreen
import com.danchoo.sample.ui.theme.GlideImageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("_SMY", "screen size MainActivity ${getScreenSize(this)}")
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


    fun getScreenSize(activity: Activity): Point? {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

}
