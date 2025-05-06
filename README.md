# KMP Native Flavor Plugin

A Gradle plugin for Kotlin Multiplatform projects that adds support for building native targets with different flavors.

## Features

- Define multiple flavors for your KMP native targets
- Configure different targets and build types for each flavor
- Automatically generate Gradle tasks for each flavor
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
    id("com.rsicarelli.kmp-native-flavor") version "1.0.0-SNAPSHOT"
}
```

## Usage

### Basic Configuration

```kotlin
kmpNativeFlavor {
    flavors {
        register("production") {
            targets = setOf("iosArm64")
            buildType = org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeBuildType.RELEASE
        }
        register("development") {
            targets = setOf("iosSimulatorArm64") 
            buildType = org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeBuildType.DEBUG
        }
        register("qa") {
            targets = setOf("iosArm64", "iosSimulatorArm64")
            buildType = "debug" // String version also works
        }
    }
}
```

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