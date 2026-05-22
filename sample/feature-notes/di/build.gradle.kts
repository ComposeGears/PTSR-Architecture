plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":feature-notes:tasks"))
    implementation(project(":feature-notes:solutions"))
    implementation(libs.leviathan)
}

