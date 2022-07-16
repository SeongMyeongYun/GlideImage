plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("maven-publish")
}

android {
    default()

    composeOptions.run {
        kotlinCompilerExtensionVersion = Config.COMPOSE_VERSION
    }

    compileSdk = 31

    defaultConfig.run {
        versionCode = Config.VERSION_CODE
        versionName = Config.VERSION_NAME
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.run {
        compose = true
        buildConfig = false
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }

        animationsDisabled = true
    }
    namespace = "com.danchoo.glideimage"

    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("maven") {
                    groupId = GlideImageConfig.GROUP_ID
                    artifactId = GlideImageConfig.ARTIFACT_ID
                    version = Config.VERSION_NAME
                    artifact("$buildDir/outputs/aar/${artifactId}-release.aar")
                }
            }
        }
    }
}

object GlideImageConfig {
    const val GROUP_ID = "com.github.danchoo21"
    const val ARTIFACT_ID = "glide-image"
}

dependencies {
    androidTestImplementation(project(":imageloading-testutils"))

    AndroidX.run {
        implementation(coreKtx)
        implementation(appcompat)
    }

    Glide.run {
        implementation(glide)
        implementation(integration)
        implementation(webpDecoder)
        implementation(transformations)
        kapt(compiler)
    }

    AndroidX.Compose.run {
        implementation(ui)
        implementation(runtime)
        implementation(material)
        implementation(foundation)

        implementation(tooling)

        debugImplementation(tooling)
        androidTestImplementation(uiTestManifest)
        androidTestImplementation(uiTest)
        androidTestImplementation(uiTestJunit)
    }

    AndroidX.Test.run {
        androidTestImplementation(runner)
    }

    Google.run {
        implementation(material)
        androidTestImplementation(truth)
    }


    JUnit4.run {
        testImplementation(junit)
    }

    Accompanist.run {
        api(drawablePainter)
    }

    Okhttp.run {
        implementation(okhttp)
    }
}
