
import java.util.Properties
import java.text.SimpleDateFormat
import java.util.Date

fun versionCodeDate(): Int {
    val dateFormat = SimpleDateFormat("yyMMdd")
    return dateFormat.format(Date()).toInt()
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
//    splits {
//        abi {
//            isEnable = false
//        }
//        density {
//            isEnable = false
//        }
//    }

    namespace = "com.walhalla.horoscope"

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    signingConfigs {
        create("config0") {
            keyAlias = "release"
            keyPassword = "release"
            storeFile = file("keystore/keystore.jks")
            storePassword = "release"
        }
    }

    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.android.buildTools.get()

    val versionPropsFile = file("version.properties")
    if (versionPropsFile.canRead()) {
        defaultConfig {
            resourceConfigurations += listOf("ru")

            vectorDrawables {
                useSupportLibrary = true
            }

            val code = versionCodeDate()
            applicationId = "com.walhalla.horoscope"
            minSdk = libs.versions.android.minSdk.get().toInt()
            targetSdk = libs.versions.android.targetSdk.get().toInt()
            versionCode = code
            versionName = "1.3.$code"
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            setProperty("archivesBaseName", "Horoscope")
        }
    } else {
        throw GradleException("Could not read version.properties!")
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("config0")
            versionNameSuffix = "-DEBUG"
        }

        getByName("release") {
            isDebuggable = false
            multiDexEnabled = true
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isJniDebuggable = false
            signingConfig = signingConfigs.getByName("config0")
            //isRenderscriptDebuggable = false
            isPseudoLocalesEnabled = true
            versionNameSuffix = ".release"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // AndroidX
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.multidex)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.process)

    // Firebase
    //implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ads)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Third-party
    implementation(libs.bigimageviewer) {
        exclude(group = "com.android.support")
    }
    implementation(libs.glide) {
        exclude(group = "com.android.support")
    }
    implementation(libs.dagger)
    implementation(libs.jsr305) {
        exclude(group = "com.android.support")
    }
    implementation(libs.lottie)
    implementation(libs.kotlin.stdlib.jdk8)

    // Project modules
    implementation(project(":scoop"))
    implementation(project(":threader"))
    implementation(project(":library"))
    implementation(project(":features:ui"))
    implementation(project(":features:wads"))

    // Annotation processors
    annotationProcessor(libs.dagger.compiler)
}
