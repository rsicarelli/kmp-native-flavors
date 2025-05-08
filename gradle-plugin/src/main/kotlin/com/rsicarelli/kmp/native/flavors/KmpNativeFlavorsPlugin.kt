package com.rsicarelli.kmp.native.flavors

import com.rsicarelli.kmp.native.flavors.tasks.KmpNativeBuildFlavorsTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskProvider
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.findByType

/**
 * A Gradle plugin that adds support for building KMP native targets with different flavors.
 * This plugin creates tasks for building different flavors of KMP native targets,
 * each with their own configuration like build type and target platform.
 *
 * When applied to the root project, the configuration is shared with all subprojects.
 */
class KmpNativeFlavorsPlugin : Plugin<Project> {

    companion object {
        // Key used to store flavor configurations in the root project extensions
        const val SHARED_FLAVOR_CONFIG_KEY = "kmpNativeFlavorSharedConfig"
    }

    override fun apply(project: Project) {
        val extension = project.extensions.create<KmpNativeFlavorsExtension>("kmpNativeFlavors")

        // Store the extension in the root project's extra properties for sharing
        if (project.rootProject == project) {
            project.rootProject.extensions.extraProperties.set(SHARED_FLAVOR_CONFIG_KEY, extension)
        }

        // Apply configuration once the project is evaluated
        project.afterEvaluate {
            // Check if this is a KMP project
            project.plugins.withType(KotlinMultiplatformPluginWrapper::class.java) {
                val configExtension = if (project.rootProject != project &&
                    project.rootProject.extensions.extraProperties.has(SHARED_FLAVOR_CONFIG_KEY)
                ) {
                    // Use configuration from root project if available
                    project.rootProject.extensions.extraProperties.get(SHARED_FLAVOR_CONFIG_KEY) as KmpNativeFlavorsExtension
                } else {
                    // Otherwise use this project's own configuration
                    extension
                }

                configurePlugin(project, configExtension)
            }
        }
    }

    private fun configurePlugin(project: Project, extension: KmpNativeFlavorsExtension) {
        val kotlin = project.extensions.findByType(KotlinMultiplatformExtension::class.java)
            ?: throw GradleException("Kotlin Multiplatform plugin is not applied")

        val flavorTasks = mutableMapOf<String, TaskProvider<KmpNativeBuildFlavorsTask>>()

        extension.flavors.forEach { flavor ->
            val flavorName = flavor.name
            val targets = flavor.targets.get()
            val buildType = flavor.buildType.get()

            // Create a task for this flavor
            val taskName = "kmpNativeBuildFlavor${flavorName.capitalized()}"
            val task = project.tasks.register(taskName, KmpNativeBuildFlavorsTask::class.java) {
                this.flavorName.set(flavorName)
                this.targetNames.set(targets)
                this.buildType.set(buildType)
                this.description = "Builds the $flavorName flavor of KMP native targets"
                this.group = "kmp native flavors"
            }

            flavorTasks[flavorName] = task
        }

        // Create an aggregate task to build all flavors
        project.tasks.register("kmpNativeBuildAllFlavors") {
            this.description = "Builds all flavors of KMP native targets"
            this.group = "kmp native flavors"
            this.dependsOn(flavorTasks.values)
        }
    }
}