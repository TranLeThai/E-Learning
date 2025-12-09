plugins {
    id("com.android.application")
    // Nếu bạn dùng version catalog (libs.versions.toml) thì giữ lại dòng alias, còn không thì bỏ
    // alias(libs.plugins.android.application)

    // Quan trọng nhất: phải apply plugin này ở module app
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.tutorial"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tutorial"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.3")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Firebase BoM (khuyên dùng phiên bản mới nhất)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

    // Firebase Auth cho Java (không cần -ktx vì bạn dùng Java)
    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation("com.google.firebase:firebase-database")

    // Nếu sau này muốn thêm các dịch vụ khác:
    // implementation("com.google.firebase:firebase-firestore")
    // implementation("com.google.firebase:firebase-analytics")
    // implementation("com.google.firebase:firebase-messaging")
}