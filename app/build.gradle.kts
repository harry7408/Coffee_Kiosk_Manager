plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.choi.coffeekioskmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.choi.coffeekioskmanager"
        minSdk = 23
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding=true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // FlowBinding
    val flowbinding_version = "1.2.0"
    implementation("io.github.reactivecircus.flowbinding:flowbinding-android:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-material:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-activity:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-appcompat:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-core:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-lifecycle:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-navigation:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-preference:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-recyclerview:${flowbinding_version}")
    implementation("io.github.reactivecircus.flowbinding:flowbinding-viewpager2:${flowbinding_version}")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.9.1")
}