plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    // Di wires Tasks to Solutions — it is the only layer that sees both.
    // Models are visible transitively via tasks' 'api' dependency.
    implementation(project(":feature-counter:tasks"))
    implementation(project(":feature-counter:solutions"))
    implementation(libs.leviathan)
}

