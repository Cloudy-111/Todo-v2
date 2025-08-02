import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
}

var cloudinaryPropertiesFile = rootProject.file(".env");
val cloudinaryProperties = Properties().apply {
    if (cloudinaryPropertiesFile.exists()) {
        load(cloudinaryPropertiesFile.inputStream())
    } else {
        println("⚠️ File .env not found!")
    }
}

android {
    namespace = "com.example.todo_listv2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.todo_listv2"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CLOUD_NAME", "\"${cloudinaryProperties["CLOUD_NAME"]}\"")
        buildConfigField("String", "API_KEY", "\"${cloudinaryProperties["API_KEY"]}\"")
        buildConfigField("String", "API_SECRET", "\"${cloudinaryProperties["API_SECRET"]}\"")
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
    viewBinding {
        enable = true
    }
    dataBinding {
        enable = true
    }
    buildFeatures{
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.3.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
//    implementation("com.github.QuadFlask:colorpicker:0.0.15")
    implementation("com.github.skydoves:colorpickerview:2.2.4")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.cloudinary:cloudinary-android:2.3.1")
}