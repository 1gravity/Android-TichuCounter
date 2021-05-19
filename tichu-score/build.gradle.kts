plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.github.triplet.play")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        applicationId = "com.onegravity.tichucount"
        minSdkVersion(18)
        targetSdkVersion(30)
        val props = project.properties
        versionCode = if (props.containsKey("BUILD_NUMBER")) props["BUILD_NUMBER"].toString().toInt() else 5
        versionName = "1.2"

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
            debuggable(true)
            minifyEnabled(false)
            isShrinkResources = false
            signingConfig = signingConfigs.getByName(name)
        }
        getByName("release") {
            debuggable(false)
            minifyEnabled(true)
            isShrinkResources = true
            signingConfig = signingConfigs.getByName(name)

            ext["enableCrashReporting"] = true
            manifestPlaceholders["enableCrashReporting"] = true
            proguardFiles("proguard-rules.pro")
        }
    }

    flavorDimensions("default")

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
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.0")
    implementation("androidx.core:core-ktx:1.6.0-beta01")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    // Conductor
    implementation("com.bluelinelabs:conductor:3.0.0")
    implementation("com.bluelinelabs:conductor-viewpager2:3.0.0")

    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:3.0.7")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("com.github.akarnokd:rxjava3-extensions:3.0.1")

    // Toothpick
    implementation("com.github.stephanenicolas.toothpick:ktp:3.1.0")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:3.1.0")

    // Room
    implementation("androidx.room:room-runtime:2.4.0-alpha02")
    implementation("androidx.room:room-ktx:2.4.0-alpha02")
    implementation("androidx.room:room-rxjava3:2.4.0-alpha02")
    kapt("androidx.room:room-compiler:2.4.0-alpha02")

    // Stetho
    val developmentApi by configurations
    val developmentImplementation by configurations
    developmentApi("com.facebook.stetho:stetho:1.5.1")
    developmentImplementation("com.facebook.stetho:stetho-js-rhino:1.5.1")

    // Testing
    val testImplementation by configurations
    val androidTestImplementation by configurations
    testImplementation("junit:junit:4.13.1")
    testImplementation("org.mockito:mockito-core:3.6.28")
    testImplementation("androidx.test:core:1.3.0")
    testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing-junit5:3.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
}

play {
    val apiKeyFile = project.property("ONEGRAVITY_GOOGLE_PLAY_API_KEY").toString()
    serviceAccountCredentials.set(file(apiKeyFile))
    track.set("internal")
}
