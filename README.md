# KMP Native Flavor Plugin

A Gradle plugin for Kotlin Multiplatform projects that adds support for building native targets with different flavors.

## Features

- Define multiple flavors for your KMP native targets
- Configure different targets and build types for each flavor
- Automatically generate Gradle tasks for each flavor
- **Dynamically configure targets and source sets from flavor definitions**
- Support for environment detection (CI vs local)
- Configuration overrides via local.properties
- Full compatibility with Gradle Configuration Cache

## Requirements

- Gradle 8.10+
- Kotlin 2.1.20+
- Kotlin Multiplatform plugin applied to your project

## Setup

### Adding the plugin to your project

```kotlin
// settings.gradle.kts
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        // Add your repository here if distributing privately
    }
}

// build.gradle.kts
plugins {
    kotlin("multiplatform") version "2.1.20"
    id("com.rsicarelli.kmp-native-flavors") version "1.0.0-SNAPSHOT"
}
```

## Usage

### Basic Configuration

```kotlin
kmpNativeFlavors {
    flavors {
        register("production") {
            targets("iosArm64")
            buildType(NativeBuildType.RELEASE)
        }
        register("development") {
            targets("iosSimulatorArm64") 
            buildType(NativeBuildType.DEBUG)
        }
        register("qa") {
            targets("iosArm64", "iosSimulatorArm64")
            buildType("debug") // String version also works
        }
    }
}
```

### Dynamic Target Configuration

Instead of manually configuring your KMP targets, you can now use the `configureFromFlavors()` extension to automatically set up targets and source sets based on your flavor definitions:

```kotlin
kotlin {
    // This will automatically set up all targets defined in your flavors
    configureFromFlavors()
    
    // No need to manually call iosArm64(), iosSimulatorArm64(), etc.
    // No need to manually configure source sets
    
    // You can still customize other aspects of your multiplatform configuration
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }
    }
}

kmpNativeFlavors {
    flavors {
        // Your flavor definitions here
    }
}
```

This approach offers several benefits:
- **Reduced boilerplate**: No need to duplicate target declarations
- **Automatic source set configuration**: Source sets are automatically set up with appropriate dependencies
- **Single source of truth**: Your flavor definitions drive the entire configuration
- **More maintainable**: Adding or removing targets only requires changing your flavor definitions

### Generated Tasks

For each flavor defined in the configuration, the plugin generates a Gradle task:

- `kmpNativeBuildFlavorProduction`
- `kmpNativeBuildFlavorDevelopment`
- `kmpNativeBuildFlavorQa`

Additionally, there's an aggregate task that builds all flavors:

- `kmpNativeBuildAllFlavors`

### Overriding Configuration

You can override targets using:

1. Environment variables:
   ```
   KMP_NATIVE_TARGETS=iosArm64,iosSimulatorArm64
   ```

2. In CI environments:
   ```
   CI_KMP_NATIVE_TARGETS=iosArm64
   ```

3. Via local.properties:
   ```properties
   kmp.native.targets=iosSimulatorArm64
   ```

## Development

### Building the plugin

```bash
./gradlew build
```

### Running tests

```bash
./gradlew test
```

### Publishing locally

```bash
./gradlew publishToMavenLocal
```

## License

[MIT License](LICENSE)