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

        vectorDrawables {
            useSupportLibrary = true
        }

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
    namespace = "com.google.accompanist.imageloading.test"
}

dependencies {
    AndroidX.run {
        implementation(coreKtx)
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
        implementation(uiTestJunit)
    }

    Okhttp.run {
        implementation(okhttp)
        api(mockWebserver)
    }

    Google.run {
        implementation(truth)
    }

    Coroutines.run {
        implementation(core)
        implementation(test)
    }

    JUnit4.run {
        testImplementation(junit)
    }
}