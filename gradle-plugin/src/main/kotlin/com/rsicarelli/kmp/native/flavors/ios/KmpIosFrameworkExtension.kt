package com.rsicarelli.kmp.native.flavors.ios

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import javax.inject.Inject

/**
 * Extension for configuring iOS frameworks generation from KMP flavors.
 */
open class KmpIosFrameworkExtension @Inject constructor(private val objectFactory: ObjectFactory) {

    /**
     * Base name for the framework.
     * The flavor name will be appended to this base name.
     * Default is "SharedKit".
     */
    val frameworkBaseName: Property<String> = objectFactory.property(String::class.java)
        .convention("SharedKit")

    /**
     * Optional configurator for additional framework settings.
     * This allows for custom configuration of each framework.
     */
    var frameworkConfigurator: ((Framework) -> Unit)? = null

    /**
     * Configure the framework with custom settings.
     */
    fun configureFramework(configurator: (Framework) -> Unit) {
        frameworkConfigurator = configurator
    }
}