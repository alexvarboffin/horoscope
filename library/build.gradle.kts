plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.walhalla.horolib"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.multidex)

    // Material
    implementation(libs.material)

    // Dagger
    api(libs.dagger)
    annotationProcessor(libs.dagger.compiler)

    // Network
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // UI
    api("io.github.inflationx:viewpump:2.1.1")
    api(libs.androidx.preference.ktx)
    implementation(libs.calligraphy3) {
        exclude(group = "com.android.support")
    }

    // Room
    //implementation(libs.androidx.room.runtime)
    //implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)

    // Local projects
    implementation(project(":scoop"))
    implementation(project(":features:ui"))
    implementation(project(":threader"))
}