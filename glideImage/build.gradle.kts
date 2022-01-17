plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("maven-publish")
}

android {
    compileSdk = 31

    composeOptions.run {
        kotlinCompilerExtensionVersion = "1.0.5"
    }

    defaultConfig.run {
        minSdk = 21
        targetSdk = 31
        versionCode = 112
        versionName = "0.1.12"

        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "/META-INF/AL2.0",
                "/META-INF/LGPL2.1",
                "/META-INF/DEPENDENCIES.txt",
                "/META-INF/LICENSE.txt",
                "/META-INF/NOTICE.txt",
                "/META-INF/NOTICE",
                "/META-INF/LICENSE",
                "/META-INF/DEPENDENCIES",
                "/META-INF/notice.txt",
                "/META-INF/license.txt",
                "/META-INF/dependencies.txt",
            )
        )
    }

    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("maven") {
                    groupId = "com.github.danchoo21"
                    artifactId = "glideImage"
                    version = "0.1"
                    artifact("$buildDir/outputs/aar/${artifactId}-release.aar")
                }
            }
        }
    }
}


dependencies {
    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")

    implementation("androidx.compose.ui:ui:1.0.5")
    implementation("androidx.compose.runtime:runtime:1.0.5")
    implementation("androidx.compose.material:material:1.0.5")
    
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.4.1")


//    testImplementation 'junit:junit:4.13.2'
//    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
}