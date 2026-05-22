plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    // 'api' so that Presentation sees Models transitively via Tasks
    api(project(":feature-notes:models"))
    // Flow lives in coroutines-core
    api(libs.coroutines.core)
}

