import com.rsicarelli.kmp.native.flavors.configureFromFlavors

plugins {
    kotlin("multiplatform")
    id("com.rsicarelli.kmp-native-flavors")
}

kotlin {
    // Use the extension to dynamically configure targets from flavors
    configureFromFlavors()

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        // Note: You no longer need to manually configure your platform-specific source sets
        // They are automatically configured by the configureFromFlavors() extension
    }
}