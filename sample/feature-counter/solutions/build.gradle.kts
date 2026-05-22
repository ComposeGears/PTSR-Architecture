plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    // Solutions implement the Tasks contract
    implementation(project(":feature-counter:tasks"))
}

