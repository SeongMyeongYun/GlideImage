plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    default()

    kotlinOptions {
        jvmTarget = Config.JAVA_VERSION.toString()

        freeCompilerArgs = freeCompilerArgs.plus(
            listOf(
                "-Xopt-in=kotlin.RequiresOptIn",
                "-Xopt-in=kotlin.Experimental",
                "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi",
                "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi"
            )
        )
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

    AndroidX.Paging.run {
        implementation(ktx)
        implementation(compose)
    }

    Hilt.run {
        implementation(android)
        implementation(compose)
        kapt(compiler)
        kaptAndroidTest(compiler)
        androidTestImplementation(testing)
    }


    Glide.run {
        implementation(glide)
        kapt(compiler)
    }

    Accompanist.run {
        implementation(permission)
    }

    Coil.run {
        implementation(coil)
    }

}