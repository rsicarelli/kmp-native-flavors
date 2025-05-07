package com.rsicarelli.sample

/**
 * Platform information interface
 */
interface Platform {
    val name: String
}

expect fun getPlatform(): Platform