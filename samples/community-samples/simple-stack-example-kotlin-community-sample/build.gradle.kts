plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("realm-android")
}

android {
    compileSdk = 31

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        applicationId = "com.community.simplestackkotlindaggerexample"
        minSdk = 16
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    //implementation(mapOf("dir" to "libs", "include" to listOf("*.jar")))

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.core:core:1.7.0")
    implementation("androidx.activity:activity:1.4.0")
    implementation("androidx.fragment:fragment-ktx:1.4.1")

    implementation(project(":simple-stack"))

    implementation("com.github.Zhuinden.simple-stack-extensions:fragments:2.2.2") {
        exclude(module = "simple-stack") // only needed because of jitpack vs local
    }
    implementation("com.github.Zhuinden.simple-stack-extensions:fragments-ktx:2.2.2") {
        exclude(module = "simple-stack") // only needed because of jitpack vs local
    }
    implementation("com.github.Zhuinden.simple-stack-extensions:navigator-ktx:2.2.2") {
        exclude(module = "simple-stack") // only needed because of jitpack vs local
    }

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.google.dagger:dagger:2.39")

    kapt("com.google.dagger:dagger-compiler:2.39")

    implementation("com.squareup.retrofit2:retrofit:2.9.0") {
        // exclude Retrofit’s OkHttp peer-dependency module and define your own module import
        exclude(module = "okhttp")
    }
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.realm:android-adapters:3.0.0")
}
