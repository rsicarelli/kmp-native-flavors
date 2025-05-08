import com.rsicarelli.kmp.native.flavors.configureFromFlavors

plugins {
    kotlin("multiplatform")
}

kotlin {
    // Usa a extensão para configurar automaticamente os targets a partir das flavors definidas no root
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
    }
}