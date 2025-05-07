package com.rsicarelli.kmp.buildlogic

import com.rsicarelli.kmp.native.flavors.KmpNativeFlavorsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

/**
 * Convention plugin that configures Kotlin Multiplatform with KMP native flavors.
 * This demonstrates using the KMP native flavors plugin through a custom Gradle plugin.
 */
class KmpSampleConventionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply the KMP native flavors plugin
            apply(plugin = "com.rsicarelli.kmp-native-flavors")
            
            // Configure Kotlin Multiplatform
            plugins.withId("org.jetbrains.kotlin.multiplatform") {
                extensions.configure<KotlinMultiplatformExtension> {
                    // Configure iOS targets
                    iosArm64()
                    iosSimulatorArm64()
                    
                    // Configure iOS source sets
                    sourceSets.apply {
                        getByName("iosArm64Main") {
                            dependsOn(getByName("iosMain"))
                        }
                        getByName("iosSimulatorArm64Main") {
                            dependsOn(getByName("iosMain"))
                        }
                        getByName("iosArm64Test") {
                            dependsOn(getByName("iosTest"))
                        }
                        getByName("iosSimulatorArm64Test") {
                            dependsOn(getByName("iosTest"))
                        }
                    }
                }
            }
            
            // Configure KMP native flavors
            extensions.configure<KmpNativeFlavorsExtension> {
                flavors {
                    // Development flavor (for local development)
                    register("development") {
                        targets("iosSimulatorArm64")
                        buildType(NativeBuildType.DEBUG)
                    }
                    
                    // Production flavor (for release builds)
                    register("production") {
                        targets("iosArm64")
                        buildType(NativeBuildType.RELEASE)
                    }
                    
                    // Testing flavor (both simulator and device)
                    register("testing") {
                        targets("iosSimulatorArm64", "iosArm64")
                        buildType("debug") // Using string version
                    }
                }
            }
        }
    }
}