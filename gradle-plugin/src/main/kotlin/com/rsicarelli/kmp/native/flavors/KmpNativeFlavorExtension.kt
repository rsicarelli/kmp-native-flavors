package com.rsicarelli.kmp.native.flavor

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import javax.inject.Inject

/**
 * Extension for configuring KMP native flavors.
 */
open class KmpNativeFlavorExtension @Inject constructor(private val objectFactory: ObjectFactory) {

    /**
     * Container for flavor configurations.
     */
    val flavors: NamedDomainObjectContainer<FlavorConfig> =
        objectFactory.domainObjectContainer(FlavorConfig::class.java) { name ->
            objectFactory.newInstance<FlavorConfig>(name)
        }

    /**
     * Configure the flavors in this extension.
     */
    fun flavors(action: Action<NamedDomainObjectContainer<FlavorConfig>>) {
        action.execute(flavors)
    }

    /**
     * Configuration for a specific flavor.
     */
    open class FlavorConfig @Inject constructor(val name: String, private val objectFactory: ObjectFactory) {
        /**
         * The Kotlin Native targets to build for this flavor.
         * For example: ["iosArm64", "iosSimulatorArm64"]
         */
        val targets: SetProperty<String> = objectFactory.setProperty<String>().convention(emptySet())

        /**
         * The build type to use for this flavor.
         * Valid values are from NativeBuildType: DEBUG, RELEASE
         */
        val buildType: Property<NativeBuildType> = objectFactory.property<NativeBuildType>()
            .convention(NativeBuildType.DEBUG)

        /**
         * Set the targets for this flavor.
         *
         * @param targets The native targets to build
         */
        fun targets(vararg targets: String) {
            this.targets.set(targets.toSet())
        }

        /**
         * Set the build type for this flavor.
         *
         * @param buildType The build type (DEBUG or RELEASE)
         */
        fun buildType(buildType: NativeBuildType) {
            this.buildType.set(buildType)
        }

        /**
         * Set the build type for this flavor from a string.
         *
         * @param buildType The build type as a string ("debug" or "release")
         */
        fun buildType(buildType: String) {
            val normalizedBuildType = buildType.trim().lowercase()
            val kotlinBuildType = when (normalizedBuildType) {
                "debug" -> NativeBuildType.DEBUG
                "release" -> NativeBuildType.RELEASE
                else -> throw IllegalArgumentException(
                    "Invalid build type: $buildType. " +
                            "Valid values are 'debug' or 'release'"
                )
            }
            this.buildType.set(kotlinBuildType)
        }
    }
}