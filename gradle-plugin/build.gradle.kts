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
        create("kmpNativeFlavors") {
            id = "com.rsicarelli.kmp-native-flavors"
            implementationClass = "com.rsicarelli.kmp.native.flavors.KmpNativeFlavorsPlugin"
        }
        
        // Registrando o novo plugin para iOS Framework
        create("kmpIosFramework") {
            id = "com.rsicarelli.kmp-ios-framework"
            implementationClass = "com.rsicarelli.kmp.native.flavors.ios.KmpIosFrameworkPlugin"
        }
    }
}

// Support for the Configuration Cache
tasks.withType<ValidatePlugins>().configureEach {
    enableStricterValidation.set(true)
}