package com.rsicarelli.kmp.native.flavors

import com.rsicarelli.kmp.native.flavors.tasks.KmpNativeBuildFlavorsTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.create
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.gradle.api.GradleException
import java.util.concurrent.atomic.AtomicBoolean
import org.gradle.api.tasks.TaskProvider
import org.gradle.configurationcache.extensions.capitalized

/**
 * A Gradle plugin that adds support for building KMP native targets with different flavors.
 * This plugin creates tasks for building different flavors of KMP native targets,
 * each with their own configuration like build type and target platform.
 */
class KmpNativeFlavorsPlugin : Plugin<Project> {
    private val configured = AtomicBoolean(false)

    override fun apply(project: Project) {
        val extension = project.extensions.create<KmpNativeFlavorsExtension>("kmpNativeFlavors")

        // Make sure the Kotlin Multiplatform plugin is applied
        project.plugins.withType(KotlinMultiplatformPluginWrapper::class.java) {
            project.afterEvaluate {
                if (configured.compareAndSet(false, true)) {
                    configurePlugin(project, extension)
                }
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