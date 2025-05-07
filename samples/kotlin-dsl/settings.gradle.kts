rootProject.name = "kotlin-dsl-sample"

pluginManagement {
    repositories {
        mavenLocal() // For local plugin development testing
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    
    // Include the main plugin
    includeBuild("../..")
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}