package dev.tonholo.pokedex.buildsrc

object Dependencies {
    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
    }

    object ClassPath {
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.1.0"
        const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:${Jetpack.Hilt.version}"
    }

    object Coil {
        private const val version = "1.4.0"
        const val compose = "io.coil-kt:coil-compose:$version"
    }

    object ComposeDestinations {
        private const val version = "1.2.2-beta"
        val core = "io.github.raamcosta.compose-destinations:core:$version"
        val ksp = "io.github.raamcosta.compose-destinations:ksp:$version"
    }

    object Kotlin {
        const val version = "1.5.31"
    }

    object Material {
        // https://github.com/material-components/material-components-android/releases
        const val material = "com.google.android.material:material:1.4.0"
    }

    object Jetpack {
        object Compose {
            // https://developer.android.com/jetpack/androidx/releases/compose#versions
            const val version = "1.0.5"
            const val material = "androidx.compose.material:material:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val uiTooling = "androidx.compose.ui:ui-tooling-preview:$version"

            object Activity {
                const val activityCompose = "androidx.activity:activity-compose:1.4.0"
            }

            object Test {
                const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4:$version"
                const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
            }
        }

        object DataStore {
            const val preferences = "androidx.datastore:datastore-preferences:1.0.0"
        }

        object Hilt {
            internal const val version = "2.40.5"
            const val hilt = "com.google.dagger:hilt-android:$version"
            const val kapt = "com.google.dagger:hilt-compiler:$version"
            const val navigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-rc01"

            object Test {
                const val main = "com.google.dagger:hilt-android-testing:$version"
                const val kapt = "com.google.dagger:hilt-compiler:$version"
            }
        }

        object Lifecycle {
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"
        }

        object Navigation {
            // https://developer.android.com/jetpack/androidx/releases/navigation#kts
            const val classpath = "androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5"
            const val compose = "androidx.navigation:navigation-compose:2.4.0-rc01"
        }

        object Paging {
            private const val version = "3.1.0"
            const val runtime = "androidx.paging:paging-runtime:$version"
            const val compose = "androidx.paging:paging-compose:1.0.0-alpha14"
        }

        object Room {
            private const val version = "2.4.0"
            const val runtime = "androidx.room:room-runtime:$version"
            const val ksp = "androidx.room:room-compiler:$version"
            const val coroutines = "androidx.room:room-ktx:$version"
            const val paging = "androidx.room:room-paging:$version"
        }
    }

    object JUnit {
        private const val version = "4.13"
        const val junit = "junit:junit:$version"
    }

    object Retrofit {
        // https://github.com/square/retrofit/tags
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object Test {
        private const val version = "1.3.0"
        const val runner = "androidx.test:runner:$version"
        const val rules = "androidx.test:rules:$version"

        object Ext {
            private const val version = "1.1.3"
            const val junit = "androidx.test.ext:junit-ktx:$version"
        }

        const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
    }
}
