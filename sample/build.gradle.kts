import com.rsicarelli.kmp.native.flavors.configureFromFlavors
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    kotlin("multiplatform") version "2.0.0" apply false
    id("com.rsicarelli.kmp-native-flavors")
}

group = "com.rsicarelli.sample"
version = "1.0-SNAPSHOT"

// Configure native flavors ANTES da configuração do Kotlin
kmpNativeFlavors {
    flavors {
        // Development flavor: uses the simulator target with debug build type
        register("development") {
            targets("iosSimulatorArm64")
            buildType(NativeBuildType.DEBUG)
        }
        
        // Production flavor: uses the device target with release build type
        register("production") {
            targets("iosArm64")
            buildType(NativeBuildType.RELEASE)
        }
        
        // Testing flavor: uses both simulator and device targets with debug build type
        register("testing") {
            targets("iosSimulatorArm64", "iosArm64")
            buildType("debug") // Using string version of the build type
        }
    }
}