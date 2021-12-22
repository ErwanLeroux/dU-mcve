plugins {
    java
    id("com.github.ben-manes.versions") version "0.39.0"
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    checkConstraints = true
}