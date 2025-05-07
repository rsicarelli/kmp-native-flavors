rootProject.name = "kmp-native-flavors"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}
include(":gradle-plugin")
includeBuild("samples/plain-gradle")
includeBuild("samples/kotlin-dsl")