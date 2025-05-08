package com.rsicarelli.kmp.native.flavors

import org.gradle.api.GradleException
import org.gradle.api.logging.Logger
import org.jetbrains.kotlin.gradle.ExternalKotlinTargetApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.external.project

/**
 * Configures Kotlin Multiplatform targets dynamically based on the defined flavors.
 * This extension allows you to set up targets and sourcesets automatically instead of manually declaring them.
 *
 * Usage:
 * ```
 * kotlin {
 *     configureFromFlavors()
 *
 *     // The rest of your Kotlin Multiplatform configuration
 *     // You no longer need to manually declare targets like iosArm64(), iosSimulatorArm64() etc.
 * }
 * ```
 */
@OptIn(ExternalKotlinTargetApi::class)
fun KotlinMultiplatformExtension.configureFromFlavors() {
    // Get the flavor extension
    val extension = project.extensions.findByType(KmpNativeFlavorsExtension::class.java)
        ?: throw GradleException("KMP Native Flavors extension not found. Make sure you've applied the plugin.")

    // Collect all unique targets from all flavors
    val allTargets = mutableSetOf<String>()
    extension.flavors.forEach { flavor ->
        allTargets.addAll(flavor.targets.get())
    }

    project.logger.lifecycle("Configuring KMP targets dynamically from flavors: $allTargets")

    // Configure each target
    configureTargets(allTargets, project.logger)
}

/**
 * Configures targets based on the collected set of all targets from all flavors.
 */
private fun KotlinMultiplatformExtension.configureTargets(targets: Set<String>, logger: Logger) {
    targets.forEach { targetName ->
        when (targetName) {
            "iosArm64" -> iosArm64()
            "iosSimulatorArm64" -> iosSimulatorArm64()
            "iosX64" -> iosX64()

            // macOS targets
            "macosX64" -> macosX64()
            "macosArm64" -> macosArm64()

            // tvOS targets
            "tvosArm64" -> tvosArm64()
            "tvosSimulatorArm64" -> tvosSimulatorArm64()
            "tvosX64" -> tvosX64()

            // watchOS targets
            "watchosArm32" -> watchosArm32()
            "watchosArm64" -> watchosArm64()
            "watchosSimulatorArm64" -> watchosSimulatorArm64()
            "watchosX64" -> watchosX64()

            // Linux targets
            "linuxX64" -> linuxX64()
            "linuxArm64" -> linuxArm64()

            // Handle unknown targets
            else -> logger.warn("Unknown target: $targetName - skipping configuration")
        }
    }
}