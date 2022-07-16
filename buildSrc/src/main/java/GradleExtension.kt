import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion

object Config {
    const val APPLICATION_ID = "com.danchoo.glideimage"
    const val COMPILE_SDK = 32
    const val BUILD_TOOL_VERSION = "32.0.0"
    const val MIN_SDK = 21
    const val TARGET_SDK = 32
    const val VERSION_NAME = "1.2.2"
    const val VERSION_CODE = 100202

    const val TEST_INSTRUMENTATION_RUNNER = "com.danchoo.date.CustomTestRunner"

    val JAVA_VERSION = JavaVersion.VERSION_1_8

    const val COMPOSE_VERSION = AndroidX.Compose.version
}

fun BaseAppModuleExtension.default() {
    compileSdk = Config.COMPILE_SDK
    buildToolsVersion = Config.BUILD_TOOL_VERSION

    defaultConfig.run {
        applicationId = Config.APPLICATION_ID
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK
        versionCode = Config.VERSION_CODE
        versionName = Config.VERSION_NAME

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = Config.TEST_INSTRUMENTATION_RUNNER
    }

    buildTypes.run {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions.run {
        sourceCompatibility = Config.JAVA_VERSION
        targetCompatibility = Config.JAVA_VERSION
    }

    composeOptions.run {
        kotlinCompilerExtensionVersion = Config.COMPOSE_VERSION
    }

    packagingOptions.run {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures.run {
        compose = true

        // Disable unused AGP features
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }
}

fun LibraryExtension.default() {
    compileSdk = Config.COMPILE_SDK
    buildToolsVersion = Config.BUILD_TOOL_VERSION

    defaultConfig.run {
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK

        testInstrumentationRunner = Config.TEST_INSTRUMENTATION_RUNNER

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes.run {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions.run {
        sourceCompatibility = Config.JAVA_VERSION
        targetCompatibility = Config.JAVA_VERSION
    }

    packagingOptions.run {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}
