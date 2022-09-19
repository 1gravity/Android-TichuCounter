plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.funnydevs.hilt-conductor.plugin")
    id("dagger.hilt.android.plugin")
    id("com.github.triplet.play")
}

android {
    compileSdk = 33
    buildToolsVersion = "33.0.0"

    defaultConfig {
        applicationId = "com.onegravity.tichucount"
        minSdk = 21
        targetSdk = 33
        versionCode = project.properties["BUILD_NUMBER"]
            ?.toString()?.toInt()?.minus(1643908089)
            ?: 124
        versionName = "1.5.1"

        android.defaultConfig.vectorDrawables.useSupportLibrary = true

        multiDexEnabled = true

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
        }
        create("release") {
            storeFile = file(project.property("ONEGRAVITY_KEYSTORE_FILE").toString())
            storePassword = project.property("ONEGRAVITY_KEYSTORE_PASSWORD").toString()
            keyAlias = project.property("ONEGRAVITY_TICHUSCORE_KEY_ALIAS").toString()
            keyPassword = project.property("ONEGRAVITY_TICHUSCORE_KEY_PASSWORD").toString()
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName(name)
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName(name)

            ext["enableCrashReporting"] = true
            manifestPlaceholders["enableCrashReporting"] = true
            proguardFiles("proguard-rules.pro")
        }
    }

    flavorDimensions.add("default")

    productFlavors {
        // The actual release version
        create("production") {
            dimension = "default"
        }

        // The development version
        create("development") {
            dimension = "default"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // base libraries
    implementation(AndroidX.multidex)
    implementation(Kotlin.stdlib)
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.appCompat)
    implementation(Google.android.material)
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.constraintLayout)
    implementation(KotlinX.coroutines.android)
    implementation(AndroidX.fragment.ktx)

    // Conductor
    implementation("com.bluelinelabs:conductor:_")
    implementation("com.bluelinelabs:conductor-viewpager2:_")
    implementation("com.bluelinelabs:conductor-archlifecycle:_")

    // Hilt
    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.android.compiler)
    implementation("io.github.funnydevs:hilt-conductor:_")
    kapt("io.github.funnydevs:hilt-conductor-processor:_")

    // Room
    implementation(AndroidX.room.runtime)
    implementation(AndroidX.room.ktx)
    kapt(AndroidX.room.compiler)

    // Stetho
    val developmentApi by configurations
    val developmentImplementation by configurations
    developmentApi("com.facebook.stetho:stetho:_")
    developmentImplementation("com.facebook.stetho:stetho-js-rhino:_")

    // Testing
    val testImplementation by configurations
    val androidTestImplementation by configurations
    testImplementation(Testing.junit4)
    testImplementation(Testing.mockito.core)
    testImplementation(AndroidX.test.core)
    testImplementation(KotlinX.coroutines.test)
    testImplementation(CashApp.turbine)

    kaptTest(Google.dagger.hilt.android.compiler)
    androidTestImplementation(AndroidX.test.ext.junit)
    kaptAndroidTest(Google.dagger.hilt.android.compiler)
}

// Gradle Play Publisher
play {
    val apiKeyFile = project.property("ONEGRAVITY_GOOGLE_PLAY_API_KEY").toString()
    serviceAccountCredentials.set(file(apiKeyFile))
    track.set("internal")
}
