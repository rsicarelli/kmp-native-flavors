# KMP Native Flavors - Kotlin DSL Sample

This sample demonstrates how to use the KMP Native Flavors plugin with a more structured Kotlin DSL approach. This is similar to using a convention plugin but keeps everything in a single build file for simplicity.

## Project Structure

```
src/
├── commonMain/           # Common Kotlin code
├── iosMain/              # iOS-specific implementations
```

## Setup

This sample applies the KMP Native Flavors plugin directly, but uses the Kotlin DSL to organize the build script in a cleaner way:

```kotlin
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    kotlin("multiplatform") version "2.0.0"
    id("com.rsicarelli.kmp-native-flavors")
}

// Set up Kotlin Multiplatform
kotlin {
    // iOS targets
    iosArm64()
    iosSimulatorArm64()
    
    // Configure source sets
    sourceSets {
        // ...source set configuration...
    }
}

// Configure flavors in a separate configuration block to improve readability
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
            buildType("debug")
        }
    }
}
```

## Available Tasks

Just like the plain-gradle sample, this setup generates the following tasks:

- `kmpNativeBuildFlavorDevelopment` - Builds for iOS simulator with debug configuration
- `kmpNativeBuildFlavorProduction` - Builds for iOS device with release configuration
- `kmpNativeBuildFlavorTesting` - Builds for both simulator and device with debug configuration
- `kmpNativeBuildAllFlavors` - Builds all flavors

## Running

To build a specific flavor, run:

```bash
./gradlew kmpNativeBuildFlavorDevelopment
```

To build all flavors, run:

```bash
./gradlew kmpNativeBuildAllFlavors
```

## Notes on the Kotlin DSL Approach

This sample demonstrates how to structure your build logic in a more organized way using the Kotlin DSL. For even larger projects, you might want to consider moving this configuration to a dedicated convention plugin in a `build-logic` directory.