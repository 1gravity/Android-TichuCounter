// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")
        classpath("com.github.triplet.gradle:play-publisher:3.4.0-agp7.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
