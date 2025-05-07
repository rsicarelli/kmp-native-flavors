plugins {
    `kotlin-dsl`
}

// Configure repositories for this build script
repositories {
    mavenLocal() // For local plugin development testing
    gradlePluginPortal()
    mavenCentral()
    google()
}

// Dependencies for the convention plugins
dependencies {
    // Access to the Kotlin Multiplatform plugin
    implementation(kotlin("gradle-plugin", "2.0.0"))
    
    // Access to the KMP Native Flavors plugin - use the specific plugin ID
    implementation("com.rsicarelli.kmp-native-flavors:gradle-plugin:1.0.0-SNAPSHOT")
}