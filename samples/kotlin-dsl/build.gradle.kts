import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    kotlin("multiplatform") version "2.0.0"
    id("com.rsicarelli.kmp-native-flavors")
}

group = "com.rsicarelli.sample"
version = "1.0-SNAPSHOT"

// Set up Kotlin Multiplatform
kotlin {
    // iOS targets
    iosArm64()
    iosSimulatorArm64()
    
    // Configure source sets
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }
        
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        
        val iosMain by creating {
            dependsOn(commonMain)
        }
        
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        
        val iosTest by creating {
            dependsOn(commonTest)
        }
        
        val iosArm64Test by getting {
            dependsOn(iosTest)
        }
        
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }
}

// Configure flavors in a separate configuration block to improve readability
// This approach organizes the build script in a way similar to build-logic
// but keeps everything in a single file for simplicity
configure<com.rsicarelli.kmp.native.flavors.KmpNativeFlavorsExtension> {
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