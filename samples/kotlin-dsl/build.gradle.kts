import com.rsicarelli.kmp.native.flavors.KmpNativeFlavorsExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    kotlin("multiplatform") version "2.0.0" apply false
    id("com.rsicarelli.kmp-native-flavors")
}

group = "com.rsicarelli.sample"
version = "1.0-SNAPSHOT"

kmpNativeFlavors {
    flavors {
        // Development flavor
        register("development") {
            targets("iosSimulatorArm64")
            buildType(NativeBuildType.DEBUG)
        }

        // Production flavor
        register("production") {
            targets("iosArm64")
            buildType(NativeBuildType.RELEASE)
        }

        // Testing flavor
        register("testing") {
            targets("iosSimulatorArm64", "iosArm64")
            buildType("debug") // String version works too
        }
    }
}