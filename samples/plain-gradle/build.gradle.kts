import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    kotlin("multiplatform") version "2.0.0"
    id("com.rsicarelli.kmp-native-flavors")
}

group = "com.rsicarelli.sample"
version = "1.0-SNAPSHOT"

kotlin {
    // Target iOS platforms
    iosArm64()
    iosSimulatorArm64()
    
    // Configure Kotlin/Native targets
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.all {
            // The plugin will override these settings based on the flavor configuration
            freeCompilerArgs += listOf("-Xruntime-logs=gc=info")
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                // Common dependencies
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

// Configure native flavors
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