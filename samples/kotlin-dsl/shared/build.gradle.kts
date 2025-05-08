import com.rsicarelli.kmp.native.flavors.configureFromFlavors

plugins {
    kotlin("multiplatform")
}

// Set up Kotlin Multiplatform
kotlin {
//    configureFromFlavors()
    applyDefaultHierarchyTemplate()
    
    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}