rootProject.name = "plain-gradle"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal() // For local plugin development testing
    }
    
    // Include the main plugin so we can use it in this sample
    includeBuild("..") // Updated path to point to the root project
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

// Include the shared module
include(":shared")
// Include the new ios-interop module
include(":ios-interop")