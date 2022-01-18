plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    default()

    kotlinOptions {
        jvmTarget = Config.JAVA_VERSION.toString()
    }
}

dependencies {
    implementation(project(":glide-image"))

    AndroidX.run {
        implementation(coreKtx)
        implementation(appcompat)
        androidTestImplementation(coreTesting)
    }

    Google.run {
        implementation(material)
    }

    AndroidX.Compose.run {
        implementation(ui)
        implementation(material)
        implementation(tooling)
        debugImplementation(tooling)
    }

    AndroidX.Lifecycle.run {
        implementation(core)
        implementation(runtime)
    }

    AndroidX.Activity.run {
        implementation(compose)
    }

    AndroidX.Test.run {
        androidTestImplementation(junit)
        androidTestImplementation(espressoCore)
    }

    JUnit4.run {
        testImplementation(junit)
    }
}