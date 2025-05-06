import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    `kotlin-dsl`
    `maven-publish`
    id("java-gradle-plugin")
}

group = "com.rsicarelli"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.gradlePluginApi)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.gradle.testKit)
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

gradlePlugin {
    plugins {
        create("kmpNativeFlavor") {
            id = "com.rsicarelli.kmp-native-flavor"
            implementationClass = "com.rsicarelli.kmp.native.flavor.KmpNativeFlavorPlugin"
        }
    }
}

// Support for the Configuration Cache
tasks.withType<ValidatePlugins>().configureEach {
    enableStricterValidation.set(true)
}