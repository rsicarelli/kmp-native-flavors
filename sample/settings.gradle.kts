rootProject.name = "sample"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    // Include the main plugin so we can use it in this sample
    includeBuild("..")
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

// Include the shared module
include(":shared")