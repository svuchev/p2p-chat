plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
}

android {

    namespace = "com.stoyanvuchev.p2pchat"
    compileSdk = 35

    defaultConfig {

        applicationId = "com.stoyanvuchev.p2pchat"

        minSdk = 23
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }

}

dependencies {

    implementation(libs.bundles.core)
    testImplementation(libs.bundles.coreTest)
    androidTestImplementation(libs.bundles.coreAndroidTest)

    implementation(libs.bundles.lifecycle)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.composeBom)
    
    debugImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.bundles.composeBomDebug)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.composeBomAndroidTest)

    implementation(libs.bundles.serialization)

    implementation(libs.bundles.dependencyInjection)
    ksp(libs.bundles.dependencyInjectionKsp)

    implementation(libs.bundles.other)

}