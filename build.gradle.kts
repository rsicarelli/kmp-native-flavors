// Root project build file
// This file is intentionally minimal as the actual implementation is in the gradle-plugin module

tasks.register("clean") {
    group = "build"
    description = "Cleans all modules"
    dependsOn(gradle.includedBuilds.map { it.task(":clean") })
}

tasks.register("build") {
    group = "build"
    description = "Builds all modules"
    dependsOn(gradle.includedBuilds.map { it.task(":build") })
}