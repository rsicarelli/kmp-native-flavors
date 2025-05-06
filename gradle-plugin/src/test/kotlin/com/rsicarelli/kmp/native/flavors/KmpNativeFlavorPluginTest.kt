package com.rsicarelli.kmp.native.flavor

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KmpNativeFlavorPluginTest {

    @Rule
    @JvmField
    val testProjectDir = TemporaryFolder()

    @Test
    fun `plugin registers tasks for each flavor`() {
        // Set up test project
        val buildFile = testProjectDir.newFile("build.gradle.kts")
        val settingsFile = testProjectDir.newFile("settings.gradle.kts")
        
        settingsFile.writeText("""
            rootProject.name = "test-project"
            
            pluginManagement {
                repositories {
                    mavenLocal()
                    gradlePluginPortal()
                    mavenCentral()
                    google()
                }
            }
        """.trimIndent())

        buildFile.writeText("""
            plugins {
                kotlin("multiplatform") version "2.0.21"
                id("com.rsicarelli.kmp-native-flavor")
            }
            
            repositories {
                mavenCentral()
                google()
            }
            
            kotlin {
                iosArm64()
                iosSimulatorArm64()
                
                sourceSets {
                    val commonMain by getting
                    val iosMain by creating {
                        dependsOn(commonMain)
                    }
                    val iosArm64Main by getting {
                        dependsOn(iosMain)
                    }
                    val iosSimulatorArm64Main by getting {
                        dependsOn(iosMain)
                    }
                }
            }
            
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
                }
            }
        """.trimIndent())

        // Run the build
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withPluginClasspath()
            .withArguments("tasks", "--all")
            .build()

        // Verify the output
        val output = result.output
        assertTrue(output.contains("kmpNativeBuildFlavorProduction"))
        assertTrue(output.contains("kmpNativeBuildFlavorDevelopment"))
        assertTrue(output.contains("kmpNativeBuildAllFlavors"))
    }
}