# KMP Native Flavors - Plain Gradle Sample

This sample demonstrates how to use the KMP Native Flavors plugin with a direct integration in your build.gradle.kts file. This approach is simpler and more straightforward for smaller projects.

## Project Structure

```
src/
├── commonMain/           # Common Kotlin code
├── iosMain/              # iOS-specific implementations
```

## Setup

This sample directly applies and configures the KMP Native Flavors plugin in the build.gradle.kts file:

```kotlin
plugins {
    kotlin("multiplatform") version "2.0.0"
    id("com.rsicarelli.kmp-native-flavors")
}

// Configure native flavors
kmpNativeFlavors {
    flavors {
        register("development") {
            targets("iosSimulatorArm64")
            buildType(NativeBuildType.DEBUG)
        }
        
        register("production") {
            targets("iosArm64")
            buildType(NativeBuildType.RELEASE)
        }
        
        register("testing") {
            targets("iosSimulatorArm64", "iosArm64")
            buildType("debug")
        }
    }
}
```

## Available Tasks

This setup generates the following tasks:

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