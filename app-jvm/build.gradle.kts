import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.tiamat.destinations)
}

sourceSets.main {
    java.srcDir("kotlin")
}

dependencies {
    implementation(libs.kotlin.coroutines.swing)
    implementation(libs.compose.foundation)
    implementation(libs.compose.preview)
    implementation(libs.tiamat.core)
    implementation(libs.tiamat.destinations)
    implementation(libs.leviathan)
    implementation(libs.leviathan.compose)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "composegears.tiamat.sample"
            packageVersion = "1.0.0"
        }
    }
}