// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Android.tools.build.gradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
        classpath("com.github.triplet.gradle:play-publisher:_")
        classpath(Google.dagger.hilt.android.gradlePlugin)
        classpath("io.github.funnydevs:hilt-conductor-plugin:_")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
