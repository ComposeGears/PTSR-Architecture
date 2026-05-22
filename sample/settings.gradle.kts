pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "sample"

include(":app")

include(":feature-counter")
include(":feature-counter:models")
include(":feature-counter:tasks")
include(":feature-counter:solutions")
include(":feature-counter:di")
include(":feature-counter:presentation")

include(":feature-notes")
include(":feature-notes:models")
include(":feature-notes:tasks")
include(":feature-notes:solutions")
include(":feature-notes:di")
include(":feature-notes:presentation")

