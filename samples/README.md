# KMP Native Flavors - Samples

This directory contains sample projects demonstrating different ways to integrate and use the KMP Native Flavors plugin in Kotlin Multiplatform projects.

## Available Samples

### 1. Plain Gradle (`plain-gradle/`)

The plain-gradle sample shows the simplest way to integrate the KMP Native Flavors plugin directly in your `build.gradle.kts` file.

**Best for:**
- Smaller projects
- Quick setup
- Direct integration

### 2. Kotlin DSL (`kotlin-dsl/`)

The kotlin-dsl sample demonstrates a more structured approach to configuring the KMP Native Flavors plugin using Kotlin DSL features.

**Best for:**
- Medium-sized projects
- More organized build configurations
- Clearer separation of concerns

## Key Features Demonstrated in Both Samples

Both samples demonstrate:

1. Configuring multiple flavors:
   - **development**: For simulator builds with debug configuration
   - **production**: For device builds with release configuration
   - **testing**: For both simulator and device with debug configuration

2. Accessing and running the generated Gradle tasks:
   - `kmpNativeBuildFlavorDevelopment`
   - `kmpNativeBuildFlavorProduction`
   - `kmpNativeBuildFlavorTesting`
   - `kmpNativeBuildAllFlavors`

3. Working with a basic KMP project structure with common and iOS-specific code.

## Prerequisites

- JDK 17+ installed
- Xcode (for iOS targets)

## Getting Started

Each sample contains its own README with specific instructions, but generally:

1. Navigate to a sample directory:
   ```bash
   cd plain-gradle
   ```

2. Run a specific flavor task:
   ```bash
   ./gradlew kmpNativeBuildFlavorDevelopment
   ```

## Choosing the Right Approach

- **Plain Gradle**: Choose this if you want the simplest integration with minimal setup.
- **Kotlin DSL**: Choose this if you prefer a more organized build script structure.

For very large projects, you might want to consider a full convention plugin approach with a dedicated `build-logic` module.