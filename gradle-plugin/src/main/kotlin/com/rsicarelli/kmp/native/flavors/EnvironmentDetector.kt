package com.rsicarelli.kmp.native.flavor

import org.gradle.api.Project
import java.io.File
import java.util.Properties

/**
 * Utility class for detecting the current environment (CI or local).
 * This helps in automatically configuring KMP native flavors based on the execution environment.
 */
object EnvironmentDetector {
    /**
     * Detects if the build is running in a CI environment.
     * 
     * @return true if the build is running in a CI environment, false otherwise
     */
    fun isCI(): Boolean {
        // Check common CI environment variables
        return listOf(
            "CI",
            "GITHUB_ACTIONS",
            "GITLAB_CI",
            "CIRCLECI",
            "TRAVIS",
            "JENKINS_URL",
            "TEAMCITY_VERSION",
            "BITBUCKET_BUILD_NUMBER"
        ).any { System.getenv(it) != null }
    }
    
    /**
     * Loads configuration overrides from local.properties file.
     * 
     * @param project The Gradle project
     * @return Properties object containing key-value pairs from local.properties, or empty Properties if file doesn't exist
     */
    fun loadLocalProperties(project: Project): Properties {
        val properties = Properties()
        val localPropertiesFile = File(project.rootDir, "local.properties")
        
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { 
                properties.load(it)
            }
        }
        
        return properties
    }
    
    /**
     * Gets the target platform configuration overrides from local properties or environment.
     * 
     * @param project The Gradle project
     * @param defaultTargets The default targets to use if no overrides are found
     * @return A set of target platforms to build for
     */
    fun getTargetPlatforms(project: Project, defaultTargets: Set<String>): Set<String> {
        // Check if targets are specified in local.properties
        val localProperties = loadLocalProperties(project)
        val localTargets = localProperties.getProperty("kmp.native.targets")
        
        if (!localTargets.isNullOrBlank()) {
            return localTargets.split(",").map { it.trim() }.toSet()
        }
        
        // Check if targets are specified in environment variables
        val envTargets = System.getenv("KMP_NATIVE_TARGETS")
        if (!envTargets.isNullOrBlank()) {
            return envTargets.split(",").map { it.trim() }.toSet()
        }
        
        // If running in CI, you might want to use a specific set of targets
        if (isCI()) {
            project.logger.lifecycle("CI environment detected, using CI-specific targets if available")
            val ciTargets = System.getenv("CI_KMP_NATIVE_TARGETS")
            if (!ciTargets.isNullOrBlank()) {
                return ciTargets.split(",").map { it.trim() }.toSet()
            }
        }
        
        // Fall back to defaults
        return defaultTargets
    }
}