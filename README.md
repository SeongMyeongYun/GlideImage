# Compose Glide Image 

Jetpack Compose image loading library.
````
use glide version : 4.12.0
github : https://github.com/bumptech/glide
````

compose glide image load.


# Setup
````kotlin
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
````

[![](https://jitpack.io/v/danchoo21/glide-image.svg)](https://jitpack.io/#danchoo21/glide-image)

````kotlin
dependencies {
    implementation 'com.github.danchoo21:glide-image:lastVersion'
    implementation 'com.google.accompanist:accompanist-drawablepainter:0.24.1-alpha'
}
````


# Use - rememberGlideImagePinter

````kotlin
@Composable
private fun TestComposable(
    data: Any?,
    width: Dp = 0.dp,
    height: Dp = 0.dp,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Crop,
    requestBuilder: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable> = { this }
) {
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
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun TestComposable(
    data: Any?,
    size: Dp = 0.dp,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Crop,
    requestBuilder: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable> = { this }
) {
    Image(
        modifier = modifier,
        painter = rememberGlideImagePinter(
            data = data,
            size = size,
            placeHolder = placeHolder,
            contentScale = contentScale,
            requestBuilder = requestBuilder
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}
````

# Use - GlideImage


````kotlin
@Composable
private fun TestComposable(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    data: Any?,
    width: Dp,
    height: Dp,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Fit,
    requestBuilder: RequestBuilder<Drawable>.() -> RequestBuilder<Drawable> = { this }
) {
    // Image size != Bitmap Size
    GlideImage(
        modifier = modifier,
        contentDescription = contentDescription,
        data = data,
        width = width,
        height = height,
        placeHolder = placeHolder,
        contentScale = contentScale,
        requestBuilder = requestBuilder
    )
}

@Composable
private fun TestComposable(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    data: Any?,
    @DrawableRes placeHolder: Int? = null,
    contentScale: ContentScale = ContentScale.Fit,
    requestBuilder: RequestBuilder<Bitmap>.() -> RequestBuilder<Bitmap> = { this }
) {
    // Image size == Bitmap Size
    // Image size 100.dp -> set bitmap size 100.dp 
    GlideImage(
        modifier = modifier.size(100.dp, 100.dp),
        contentDescription = contentDescription,
        data = data,
        placeHolder = placeHolder,
        contentScale = contentScale,
        requestBuilder = requestBuilder
    )
}
````



# Use LocalProvider

````kotlin
class GlideAppImageLoaderImpl: GlideImageLoader {
    override fun getRequestBuilder(context: Context): RequestBuilder<Drawable> {
        return GlideApp.with(context).asDrawable()
    }
    
    override fun getRequestManager(context: Context): RequestManager {
        return GlideApp.with(context)
    }
}

@Composable
fun TestComposable() {
    CompositionLocalProvider(LocalImageLoader provides GlideAppImageLoaderImpl()) {
        Image(
            painter = rememberGlideImagePinter(data = path),
            contentDescription = null
        )
    }
}

@Composable
fun TestComposable() {
    CompositionLocalProvider(LocalImageLoader provides GlideAppImageLoaderImpl()) {
        GlideImage(data = path)
    }
}

````


# License

````
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

````
