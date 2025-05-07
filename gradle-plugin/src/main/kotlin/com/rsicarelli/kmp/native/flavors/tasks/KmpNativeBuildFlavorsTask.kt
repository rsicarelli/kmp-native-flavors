package com.rsicarelli.kmp.native.flavors.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.GradleException
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Internal
import org.gradle.work.DisableCachingByDefault
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import org.gradle.api.tasks.CacheableTask
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import javax.inject.Inject

/**
 * Task to build KMP native targets with specific configurations for a given flavor.
 */
@CacheableTask
@DisableCachingByDefault(because = "Dynamic task creation makes caching complex")
abstract class KmpNativeBuildFlavorsTask : DefaultTask() {

    /**
     * The name of the flavor being built.
     */
    @get:Input
    abstract val flavorName: Property<String>

    /**
     * The names of the Kotlin Native targets to build.
     */
    @get:Input
    abstract val targetNames: SetProperty<String>

    /**
     * The build type to use for this flavor.
     */
    @get:Input
    abstract val buildType: Property<NativeBuildType>

    @TaskAction
    fun execute() {
        val kotlin = project.extensions.findByType(KotlinMultiplatformExtension::class.java)
            ?: throw GradleException("Kotlin Multiplatform plugin not applied")

        val flavor = flavorName.get()
        val targets = targetNames.get()
        val buildType = buildType.get()

        logger.lifecycle("Building flavor '$flavor' for targets $targets with build type $buildType")

        // Find and configure the targets
        val nativeTargets = mutableListOf<KotlinNativeTarget>()

        for (targetName in targets) {
            val target = kotlin.targets.findByName(targetName) as? KotlinNativeTarget
                ?: throw GradleException("Target '$targetName' does not exist or is not a Kotlin Native target")

            nativeTargets.add(target)
        }

        // Configure the build type for each target
        for (target in nativeTargets) {
            target.binaries.all {
//                it.buildType = buildType
            }
        }

        // Find and run the compile tasks for the targets
        val compileTasks = nativeTargets.map { target ->
            project.tasks.withType(KotlinNativeCompile::class.java).filter { task ->
                task.name.startsWith("compile") &&
                        task.name.contains(target.name, ignoreCase = true) &&
                        when (buildType) {
                            NativeBuildType.DEBUG -> task.name.contains("debug", ignoreCase = true)
                            NativeBuildType.RELEASE -> task.name.contains("release", ignoreCase = true)
                        }
            }
        }.flatten()

        if (compileTasks.isEmpty()) {
            throw GradleException("No compile tasks found for the specified targets and build type")
        }

        // Run the tasks
        for (task in compileTasks) {
            logger.lifecycle("Running task ${task.path} for flavor $flavor")
            task.actions.forEach { action ->
                action.execute(task)
            }
        }

        logger.lifecycle("Completed building flavor '$flavor'")
    }
}