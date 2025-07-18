plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.jacob.fruitoftek.kotlinschoolmanagement"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jacob.fruitoftek.kotlinschoolmanagement"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // WorkManager for background sync
    implementation(libs.androidx.work.ktx)

    // RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.4.0")

    // Room (core + runtime + KTX + kapt annotation processor)
    implementation ("androidx.room:room-runtime:2.7.2")
    implementation ("androidx.room:room-ktx:2.7.2")
    kapt ("androidx.room:room-compiler:2.7.2")

    // Retrofit & Networking
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")

    // Navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.9.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.9.0")

    // Glide for image loading (Kotlin: only need kapt, not annotationProcessor)
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    kapt ("com.github.bumptech.glide:compiler:4.15.1")

    // AndroidX
    implementation ("androidx.fragment:fragment-ktx:1.8.8")

    // Circular ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")
}