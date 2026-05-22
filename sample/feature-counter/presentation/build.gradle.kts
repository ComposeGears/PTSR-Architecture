plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    // Tasks (and Models transitively) — the only external contracts Presentation needs
    implementation(project(":feature-counter:tasks"))
    // Di — wires Tasks to Solutions inside this feature
    implementation(project(":feature-counter:di"))
    // Navigation + retain
    implementation(libs.tiamat)
    // DI in Compose
    implementation(libs.leviathan.compose)
    // All Compose UI (runtime, foundation, material, desktop host)
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
}


