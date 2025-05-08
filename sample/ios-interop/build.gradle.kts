import com.rsicarelli.kmp.native.flavors.configureFromFlavors

plugins {
    kotlin("multiplatform")
    // Aplicando o novo plugin para frameworks iOS
    id("com.rsicarelli.kmp-ios-framework")
}

kotlin {
    // Usa a extensão para configurar automaticamente os targets a partir das flavors definidas no root
    configureFromFlavors()

    sourceSets {
        commonMain.dependencies {
            // Adicionando a dependência como API para exportar corretamente no framework
            api(project(":shared"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

// Configura o nome e outras opções do framework iOS
kmpIosFramework {
    // Define o nome base do framework - será adicionado o nome do flavor
    frameworkBaseName.set("MyAppKit")
    
    // Opcionalmente, configurações adicionais específicas para cada framework
    configureFramework { framework ->
        // Exemplo: exportar símbolos específicos
        framework.export(project(":shared"))
        
        // Removendo as opções de linker incorretas
    }
}