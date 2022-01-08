import dev.tonholo.pokedex.buildsrc.Dependencies

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "dev.tonholo.study.pokedex"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Jetpack.Compose.version
    }
    packagingOptions {
        resources.excludes += "META-INF/AL2.0"
        resources.excludes += "META-INF/LGPL2.1"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.Material.material)
    implementation(Dependencies.Jetpack.Compose.ui)
    implementation(Dependencies.Jetpack.Compose.material)
    implementation(Dependencies.Jetpack.Compose.uiTooling)
    implementation(Dependencies.Jetpack.Lifecycle.runtime)
    implementation(Dependencies.Jetpack.Compose.Activity.activityCompose)

    implementation(Dependencies.Jetpack.Hilt.hilt)
    kapt(Dependencies.Jetpack.Hilt.kapt)

    // region [ Test dependencies ]
    testImplementation(Dependencies.JUnit.junit)
    androidTestImplementation(Dependencies.Test.Ext.junit)
    androidTestImplementation(Dependencies.Test.espressoCore)
    androidTestImplementation(Dependencies.Jetpack.Compose.Test.uiTestJunit4)
    debugImplementation(Dependencies.Jetpack.Compose.Test.uiTooling)

    // region [ Hilt ]
    androidTestImplementation(Dependencies.Jetpack.Hilt.Test.main)
    kaptAndroidTest(Dependencies.Jetpack.Hilt.Test.kapt)

    testImplementation(Dependencies.Jetpack.Hilt.Test.main)
    kaptTest(Dependencies.Jetpack.Hilt.Test.kapt)
    // endregion [ Hilt ]
    // endregion [ Test dependencies ]
}