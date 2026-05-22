plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":feature-notes:tasks"))
    implementation(project(":feature-notes:di"))
    implementation(libs.tiamat)
    implementation(libs.leviathan.compose)
    // All Compose UI (runtime, foundation, material, desktop host)
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
}


