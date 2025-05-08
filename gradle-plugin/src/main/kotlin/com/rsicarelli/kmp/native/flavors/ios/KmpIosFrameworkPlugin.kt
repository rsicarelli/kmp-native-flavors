package com.rsicarelli.kmp.native.flavors.ios

import com.rsicarelli.kmp.native.flavors.KmpNativeFlavorsExtension
import com.rsicarelli.kmp.native.flavors.KmpNativeFlavorsPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.GradleException
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework.BitcodeEmbeddingMode
import java.io.File

/**
 * Plugin to create iOS Framework and XCFramework based on KMP native flavors configuration.
 * This plugin should be applied only on modules that need to produce iOS frameworks.
 */
class KmpIosFrameworkPlugin : Plugin<Project> {

    companion object {
        // Default name for the framework output
        const val DEFAULT_FRAMEWORK_BASE_NAME = "SharedKit"
    }

    override fun apply(project: Project) {
        val extension = project.extensions.create("kmpIosFramework", KmpIosFrameworkExtension::class.java)

        // Apply configuration once the project is evaluated
        project.afterEvaluate {
            // Check if this is a KMP project
            project.plugins.withType(KotlinMultiplatformPluginWrapper::class.java) {
                // Find the flavor configuration - first from root, then from local
                val flavorExtension = findFlavorExtension(project)
                    ?: throw GradleException("KMP Native Flavors extension not found. Make sure you've applied the KmpNativeFlavorsPlugin.")

                configureIosFrameworkTasks(project, extension, flavorExtension)
            }
        }
    }

    private fun findFlavorExtension(project: Project): KmpNativeFlavorsExtension? {
        // Try to get from root project first
        if (project.rootProject.extensions.extraProperties.has(KmpNativeFlavorsPlugin.SHARED_FLAVOR_CONFIG_KEY)) {
            return project.rootProject.extensions.extraProperties.get(
                KmpNativeFlavorsPlugin.SHARED_FLAVOR_CONFIG_KEY
            ) as KmpNativeFlavorsExtension
        }

        // Then try from current project
        return project.extensions.findByType(KmpNativeFlavorsExtension::class.java)
    }

    private fun configureIosFrameworkTasks(
        project: Project,
        extension: KmpIosFrameworkExtension,
        flavorExtension: KmpNativeFlavorsExtension
    ) {
        val kotlin = project.extensions.findByType(KotlinMultiplatformExtension::class.java)
            ?: throw GradleException("Kotlin Multiplatform plugin is not applied")

        // Get framework name from extension or use default
        val frameworkBaseName = extension.frameworkBaseName.getOrElse(DEFAULT_FRAMEWORK_BASE_NAME)

        // Create a Framework for each flavor
        flavorExtension.flavors.forEach { flavor ->
            val flavorName = flavor.name
            val targets = flavor.targets.get().filter { isIosTarget(it) } // Only iOS targets
            
            if (targets.isEmpty()) {
                project.logger.lifecycle("No iOS targets found for flavor $flavorName. Skipping framework creation.")
                return@forEach
            }

            val buildType = flavor.buildType.get()
            val flavorFrameworkName = "$frameworkBaseName${flavorName.capitalized()}"
            
            // Configure framework for each target
            val frameworksByTarget = mutableMapOf<String, Framework>()
            
            targets.forEach { targetName ->
                val target = kotlin.targets.findByName(targetName)
                    ?: throw GradleException("Target $targetName not found in Kotlin Multiplatform configuration")
                
                kotlin.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java)
                    .matching { it.name == targetName }
                    .configureEach {
                        binaries {
                            framework(flavorFrameworkName) {
                                // Configure framework based on build type
                                when (buildType) {
                                    NativeBuildType.DEBUG -> {
                                        embedBitcode(BitcodeEmbeddingMode.DISABLE)
                                        isStatic = false
                                    }
                                    NativeBuildType.RELEASE -> {
                                        embedBitcode(BitcodeEmbeddingMode.BITCODE)
                                        isStatic = true
                                    }
                                }
                                
                                // Add custom framework configuration from extension
                                extension.frameworkConfigurator?.let { configurator ->
                                    configurator(this)
                                }
                                
                                // Store the framework for later XCFramework creation
                                frameworksByTarget[targetName] = this
                            }
                        }
                    }
            }
            
            // Create a task for assembling XCFramework from individual frameworks
            val xcframeworkTaskName = "assembleXcFramework${flavorName.capitalized()}"
            val xcframeworkDir = File(project.buildDir, "xcframeworks/$flavorName")
            
            // Create task to assemble XCFramework
            project.tasks.register(xcframeworkTaskName) {
                description = "Assembles the XCFramework for $flavorName flavor"
                group = "kmp ios frameworks"
                
                // Depend on all individual framework tasks
                frameworksByTarget.values.forEach { framework ->
                    dependsOn(framework.linkTaskName)
                }
                
                doLast {
                    // Clean any existing XCFramework output
                    project.delete(xcframeworkDir)
                    xcframeworkDir.mkdirs()
                    
                    // Build the xcodebuild command to create XCFramework
                    val xcframeworkCommand = mutableListOf(
                        "xcodebuild", "-create-xcframework",
                        "-output", "$xcframeworkDir/$flavorFrameworkName.xcframework"
                    )
                    
                    // Add framework paths to command
                    frameworksByTarget.values.forEach { framework ->
                        xcframeworkCommand.add("-framework")
                        xcframeworkCommand.add(framework.outputDirectory.absolutePath + "/${framework.outputFile.name}")
                    }
                    
                    // Execute the xcodebuild command
                    project.exec {
                        commandLine(xcframeworkCommand)
                    }
                    
                    project.logger.lifecycle("Created XCFramework at: $xcframeworkDir/$flavorFrameworkName.xcframework")
                }
            }
        }
        
        // Create task to assemble all XCFrameworks
        project.tasks.register("assembleAllXcFrameworks") {
            description = "Assembles all XCFrameworks for all flavors"
            group = "kmp ios frameworks"
            
            // Find and depend on all XCFramework assemble tasks
            val xcFrameworkTasks = project.tasks.names
                .filter { it.startsWith("assembleXcFramework") && it != "assembleAllXcFrameworks" }
            
            dependsOn(xcFrameworkTasks)
        }
    }
    
    private fun isIosTarget(targetName: String): Boolean {
        return targetName == "iosArm64" || targetName == "iosX64" || targetName == "iosSimulatorArm64"
    }
}