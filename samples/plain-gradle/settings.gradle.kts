rootProject.name = "plain-gradle-sample"

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