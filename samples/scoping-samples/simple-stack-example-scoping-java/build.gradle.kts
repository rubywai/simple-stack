plugins {
    id("com.android.application")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.zhuinden.simplestackexamplescopingjava"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    //implementation(mapOf("dir" to "libs", "include" to listOf("*.jar")))
    implementation(project(":simple-stack"))

    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    testImplementation("junit:junit:4.13.2")

    implementation("androidx.lifecycle:lifecycle-common-java8:2.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.4.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.4.1")

    implementation("com.github.Zhuinden.simple-stack-extensions:fragments:2.2.2") {
        exclude(module = "simple-stack") // only needed because of jitpack vs local
    }
    implementation("com.github.Zhuinden.simple-stack-extensions:services:2.2.2") {
        exclude(module = "simple-stack") // only needed because of jitpack vs local
    }

    implementation("com.github.Zhuinden:fragmentviewbindingdelegate-kt:1.0.0")

    annotationProcessor("frankiesardo:auto-parcel:1.0.3")

    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.jakewharton.rxrelay2:rxrelay:2.1.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel:2.4.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.4.1")

    implementation("com.github.Zhuinden:event-emitter:1.2.0")


}
