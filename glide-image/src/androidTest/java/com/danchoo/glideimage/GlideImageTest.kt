package com.danchoo.glideimage

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.test.filters.LargeTest
import com.bumptech.glide.request.RequestListener
import com.danchoo.glideimage.test.R
import com.google.accompanist.imageloading.test.ImageMockWebServer
import com.google.accompanist.imageloading.test.assertPixels
import com.google.accompanist.imageloading.test.resourceUri
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class GlideImageTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Our MockWebServer. We use a response delay to simulate real-world conditions
    private val server = ImageMockWebServer()

    @Before
    fun setup() {
        // Start our mock web server
        server.start()
    }

    @After
    fun teardown() {
        // Shutdown our mock web server
        server.shutdown()
    }

    @Test
    fun basicLoad_http() {
        var requestCompleted by mutableStateOf(false)

        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp, 128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = server.url(
                    "/image"
                )
            ) {
                listener(SimpleRequestListener {
                    requestCompleted = true
                } as RequestListener<Drawable>)
            }
        }

        composeTestRule.waitUntil(10_000) { requestCompleted }

        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
    }

    @Test
    fun basicLoad_drawableId() {
        var requestCompleted by mutableStateOf(false)
        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp, 128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = R.drawable.red_rectangle
            ) {
                listener(SimpleRequestListener {
                    requestCompleted = true
                } as RequestListener<Drawable>)
            }
        }

        composeTestRule.waitUntil(10_000) { requestCompleted }

        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .assertIsDisplayed()
            .captureToImage()
            .assertPixels(Color.Red)
    }

    @Test
    fun basicLoad_drawableUri() {
        var requestCompleted by mutableStateOf(false)

        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp, 128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = resourceUri(R.drawable.red_rectangle)
            ) {
                listener(SimpleRequestListener {
                    requestCompleted = true
                } as RequestListener<Drawable>)
            }

        }

        composeTestRule.waitUntil(10_000) { requestCompleted }

        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .assertIsDisplayed()
            .captureToImage()
            .assertPixels(Color.Red)
    }

    @Test
    fun customRequestManager_param() {
        var requestCompleted by mutableStateOf(false)

        composeTestRule.setContent {
            // Create a RequestManager with a listener which updates requestCompleted
            GlideImage(
                modifier = Modifier
                    .size(128.dp, 128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = server.url("/image").toString()
            ) {
                listener(SimpleRequestListener {
                    requestCompleted = true
                } as RequestListener<Drawable>)
            }
        }

        // Wait for the onRequestCompleted to release the latch
        composeTestRule.waitUntil(10_000) { requestCompleted }
    }

    @Test
    fun customRequestManager_ambient() {
        var requestCompleted by mutableStateOf(false)

        composeTestRule.setContent {
            // Create a RequestManager with a listener which updates requestCompleted

            GlideImage(
                modifier = Modifier
                    .size(128.dp, 128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = server.url("/image").toString()
            ) {
                listener(SimpleRequestListener {
                    requestCompleted = true
                } as RequestListener<Drawable>)
            }
        }

        // Wait for the onRequestCompleted to release the latch
        composeTestRule.waitUntil(10_000) { requestCompleted }
    }

    @Test
    fun basicLoad_switchData() {
        var data by mutableStateOf(server.url("/red"))
        var requestCompleted by mutableStateOf(false)

        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp, 128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = data.toString()
            ) {
                listener(SimpleRequestListener {
                    requestCompleted = true
                } as RequestListener<Drawable>)
            }
        }

        // Wait until the first image loads
        composeTestRule.waitUntil(10_000) { requestCompleted }

        // Assert that the content is completely Red
        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .assertIsDisplayed()
            .captureToImage()
            .assertPixels(Color.Red)

        // Now switch the data URI to the blue drawable
        requestCompleted = false
        data = server.url("/blue")

        // Wait until the second image loads
        composeTestRule.waitUntil(10_000) { requestCompleted }

        // Assert that the content is completely Blue
        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .assertIsDisplayed()
            .captureToImage()
            .assertPixels(Color.Blue)
    }

    @Test
    fun basicLoad_changeSize() {
        var requestCompleted by mutableStateOf(false)
        var size by mutableStateOf(128.dp)

        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(size)
                    .testTag(GlideImageTestTags.Image),
                data = server.url("/image").toString()
            ) {
                listener(SimpleRequestListener {
                    requestCompleted = true
                } as RequestListener<Drawable>)
            }
        }


        composeTestRule.waitUntil(10_000) { requestCompleted }

        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .assertIsDisplayed()
            .captureToImage()

        // Now change the size
        size = 256.dp

        composeTestRule.waitUntil(10_000) { requestCompleted }
        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertWidthIsEqualTo(256.dp)
            .assertHeightIsEqualTo(256.dp)
            .assertIsDisplayed()
            .captureToImage()
    }

    @Test
    fun basicLoad_nosize() {
        var requestCompleted by mutableStateOf(false)

        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .testTag(GlideImageTestTags.Image),
                data = server.url("/image").toString()
            ) {
                listener(SimpleRequestListener {
                    requestCompleted = true
                } as RequestListener<Drawable>)
            }
        }

        // Wait until the first image loads
        composeTestRule.waitUntil(10_000) { requestCompleted }

        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertWidthIsAtLeast(1.dp)
            .assertHeightIsAtLeast(1.dp)
            .assertIsDisplayed()
    }

    @Test
    fun lazycolumn_nosize() {
        var requestCompleted by mutableStateOf(false)

        composeTestRule.setContent {
            LazyColumn {
                item {
                    GlideImage(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .testTag(GlideImageTestTags.Image),
                        data = server.url("/image").toString()
                    ) {
                        listener(SimpleRequestListener {
                            requestCompleted = true
                        } as RequestListener<Drawable>)
                    }
                }
            }
        }

        // Wait until the first image loads
        composeTestRule.waitUntil(10_000) { requestCompleted }

        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertWidthIsAtLeast(1.dp)
            .assertHeightIsAtLeast(1.dp)
            .assertIsDisplayed()
    }

    @SuppressLint("CheckResult")
    @Test
    fun basicLoad_error() {
        var requestCompleted = false

        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = server.url("/noimage")
            ) {
                error(R.drawable.red_rectangle)
                listener(SimpleRequestListener {
                    requestCompleted = true
                } as RequestListener<Drawable>)
            }
        }

        // Wait until the first image loads
        composeTestRule.waitUntil(10_000) { requestCompleted }

        // Assert that the error drawable was drawn
        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .assertIsDisplayed()
            .captureToImage()
            .assertPixels(Color.Red)
    }

    @Test
    fun previewPlaceholder() {
        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = "blah",
                placeHolder = R.drawable.red_rectangle
            )
        }

        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
            .assertIsDisplayed()
            .captureToImage()
            // We're probably scaling a bitmap up in size, so increase the tolerance to 5%
            // to not fail due to small scaling artifacts
            .assertPixels(Color.Red, tolerance = 0.05f)
    }

    @Test
    fun errorStillHasSize() {
        var requestCompleted by mutableStateOf(false)

        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = server.url("/noimage").toString()
            ) {
                error(R.drawable.red_rectangle)
                listener(SimpleRequestListener {
                    requestCompleted = true
                } as RequestListener<Drawable>)
            }
        }

        // Wait until the first image loads
        composeTestRule.waitUntil(10_000) { requestCompleted }

        // Assert that the layout is in the tree and has the correct size
        composeTestRule.onNodeWithTag(GlideImageTestTags.Image)
            .assertIsDisplayed()
            .assertWidthIsEqualTo(128.dp)
            .assertHeightIsEqualTo(128.dp)
    }

    @Test(expected = IllegalArgumentException::class)
    fun data_drawable_throws() {
        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = ShapeDrawable()
            )
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun data_imagebitmap_throws() {
        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = painterResource(android.R.drawable.ic_delete)
            )
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun data_imagevector_throws() {
        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = painterResource(R.drawable.ic_android_black_24dp)
            )

        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun data_painter_throws() {
        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = ColorPainter(Color.Magenta)
            )
        }
    }

    @Test
    fun error_stoppedThenResumed() {
        composeTestRule.setContent {
            GlideImage(
                modifier = Modifier
                    .size(128.dp)
                    .testTag(GlideImageTestTags.Image),
                data = ""
            )
        }

        composeTestRule.waitForIdle()

        // Now stop the activity, then resume it
        composeTestRule.activityRule.scenario
            .moveToState(Lifecycle.State.CREATED)
            .moveToState(Lifecycle.State.RESUMED)

        // And wait for idle. We shouldn't crash.
        composeTestRule.waitForIdle()
    }
}

object GlideImageTestTags {
    const val Image = "image"
}