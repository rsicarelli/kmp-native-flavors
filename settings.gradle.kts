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
includeBuild("sample")