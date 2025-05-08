rootProject.name = "plain-gradle"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal() // For local plugin development testing
    }
    
    // Include the main plugin so we can use it in this sample
    includeBuild("../..")
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

// Include the shared module
include(":shared")