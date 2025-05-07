package com.rsicarelli.sample

/**
 * Sample app class showing platform information
 */
class App {
    fun greeting(): String {
        val platform = getPlatform()
        return "Hello from ${platform.name}!"
    }
}