package com.rsicarelli.kmp.sample.shared

/**
 * Uma classe de repositório simples que é compartilhada entre os módulos.
 * Esta classe demonstra como o módulo 'shared' pode ser utilizado pelo módulo 'ios-interop'.
 */
class SharedRepository {
    
    /**
     * Retorna alguns dados simulados.
     */
    fun getData(): String {
        return "Dados compartilhados do módulo 'shared'"
    }
    
    /**
     * Um método adicional para demonstrar a funcionalidade.
     */
    fun processData(input: String): String {
        return "Processado: $input"
    }
}