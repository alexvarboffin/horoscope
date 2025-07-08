rootProject.name = "horoscope"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()  // Primary repository for dependencies
        google()        // Required for Android-specific dependencies
        gradlePluginPortal()  // Access to Gradle plugins

//        google {
//            mavenContent {
//                includeGroupAndSubgroups("androidx")
//                includeGroupAndSubgroups("com.android")
//                includeGroupAndSubgroups("com.google")
//            }
//        }
        maven("https://maven.google.com")
        maven("https://dl.bintray.com/videolan/Android")
        mavenCentral()
        maven("https://jitpack.io")

    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
                maven("https://maven.google.com")
                maven("https://dl.bintray.com/videolan/Android")
            }
        }
        mavenCentral()
        maven("https://jitpack.io")
    }
}
include(":scoop")
include(":library")
include(":astrolife")
include(":app")
//include(":lib")

include(":features:ui")
project(":features:ui").projectDir = File("D:\\walhalla\\sdk\\android\\ui\\features\\ui\\")

include(":features:wads")
project(":features:wads").projectDir = File("D:\\walhalla\\sdk\\android\\ui\\features\\wads\\")

include(":threader")
project(":threader").projectDir = File("D:\\walhalla\\sdk\\android\\multithreader\\threader\\")
include(":shared")
project(":shared").projectDir = File("C:\\src\\Synced\\WalhallaUI\\shared\\")
