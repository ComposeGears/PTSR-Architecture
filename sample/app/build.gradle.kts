import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    // Feature entry-points (each re-exposes only its presentation layer)
    implementation(project(":feature-counter"))
    implementation(project(":feature-notes"))
    // Tiamat for root NavigationHost
    implementation(libs.tiamat)
    // All Compose UI (desktop runtime, material, foundation, etc.)
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    // Swing dispatcher for desktop main thread
    implementation(libs.coroutines.swing)
}

compose.desktop {
    application {
        mainClass = "com.example.demo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ptsr-demo"
            packageVersion = "1.0.0"
        }
    }
}


