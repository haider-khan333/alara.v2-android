import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
//    kotlin("kapt")

}

android {
    namespace = "com.ai.alarav2"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.ai.alarav2"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
//    kotlinOptions {
//        jvmTarget = "17"
//    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.hilt.android)
//    kapt(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler) // Changed from kapt to ksp

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.compose.material3.window.size.class1)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation("com.mikepenz:multiplatform-markdown-renderer-android:0.39.0")
    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.39.0")

    implementation("com.mikepenz:multiplatform-markdown-renderer-code:0.39.0")

    implementation("androidx.datastore:datastore-preferences:1.2.0")

    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.5.0-alpha13")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.0")




}