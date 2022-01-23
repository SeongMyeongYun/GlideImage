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

        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.run {
        compose = true
    }

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
    AndroidX.run {
        implementation(coreKtx)
        implementation(appcompat)
    }

    Glide.run {
        implementation(glide)
        kapt(compiler)
    }

    AndroidX.Compose.run {
        implementation(ui)
        implementation(runtime)
        implementation(material)
    }

    Google.run {
        implementation(material)
    }

    AndroidX.Test.run {
        androidTestImplementation(junit)
        androidTestImplementation(espressoCore)
    }

    JUnit4.run {
        testImplementation(junit)
    }
}