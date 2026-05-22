// Aggregator: re-exposes only the presentation layer to outside consumers.
plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(project(":feature-notes:presentation"))
    implementation(project(":feature-notes:models"))
    implementation(project(":feature-notes:tasks"))
    implementation(project(":feature-notes:solutions"))
    implementation(project(":feature-notes:di"))
}

