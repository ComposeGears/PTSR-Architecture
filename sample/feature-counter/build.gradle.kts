// Aggregator: re-exposes only the presentation layer to outside consumers.
// Solutions, di, tasks, and models are reachable only through
// the internal Gradle dependency graph (not via api).
plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(project(":feature-counter:presentation"))
    implementation(project(":feature-counter:models"))
    implementation(project(":feature-counter:tasks"))
    implementation(project(":feature-counter:solutions"))
    implementation(project(":feature-counter:di"))
}

