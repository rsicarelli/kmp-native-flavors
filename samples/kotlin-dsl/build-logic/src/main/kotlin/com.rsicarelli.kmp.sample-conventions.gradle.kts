import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

/**
 * Convention plugin that configures Kotlin Multiplatform with KMP native flavors.
 * This demonstrates using the KMP native flavors plugin through a precompiled script plugin.
 */

// Apply the required plugins
plugins {
    kotlin("multiplatform") apply false
    id("com.rsicarelli.kmp-native-flavors")
}

// Configure Kotlin Multiplatform
kotlin {
    // Configure iOS targets
    iosArm64()
    iosSimulatorArm64()
    
    // Configure iOS source sets
    sourceSets {
        getByName("iosArm64Main") {
            dependsOn(getByName("iosMain"))
        }
        getByName("iosSimulatorArm64Main") {
            dependsOn(getByName("iosMain"))
        }
        getByName("iosArm64Test") {
            dependsOn(getByName("iosTest"))
        }
        getByName("iosSimulatorArm64Test") {
            dependsOn(getByName("iosTest"))
        }
    }
}

// Configure KMP native flavors
kmpNativeFlavors {
    flavors {
        // Development flavor (for local development)
        register("development") {
            targets("iosSimulatorArm64")
            buildType(NativeBuildType.DEBUG)
        }
        
        // Production flavor (for release builds)
        register("production") {
            targets("iosArm64")
            buildType(NativeBuildType.RELEASE)
        }
        
        // Testing flavor (both simulator and device)
        register("testing") {
            targets("iosSimulatorArm64", "iosArm64")
            buildType("debug") // Using string version
        }
    }
}