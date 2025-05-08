package com.rsicarelli.kmp.sample.iossdk

import com.rsicarelli.kmp.sample.shared.SharedRepository

/**
 * Classe principal para exportação da API iOS.
 * Esta classe será exposta no framework iOS e serve como interface principal
 * para aplicativos iOS que vão consumir a biblioteca.
 */
class IosApi(private val repository: SharedRepository = SharedRepository()) {
    
    /**
     * Retorna uma mensagem de saudação formatada para iOS.
     * Esta é uma função que seria chamada a partir de código Swift.
     */
    fun getGreeting(): String {
        return "iOS SDK: ${repository.getData()}"
    }
    
    /**
     * Um exemplo de função que demonstra a compatibilidade com tipos do iOS.
     * Esta função pode ser chamada a partir de aplicativos iOS.
     */
    fun processPlatformData(appName: String, appVersion: String): Map<String, String> {
        return mapOf(
            "platform" to "iOS",
            "appName" to appName,
            "appVersion" to appVersion,
            "sdkVersion" to "1.0.0",
            "message" to repository.getData()
        )
    }
}

/**
 * Objeto singleton para facilitar o acesso a partir de Swift.
 * Objetos companion são expostos como métodos estáticos em iOS.
 */
object IosApiCompanion {
    fun create(): IosApi = IosApi()
}