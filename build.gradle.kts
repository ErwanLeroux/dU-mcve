import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    java
    id("com.github.ben-manes.versions") version "0.39.0"
}

repositories {
    mavenCentral()
}

// https://github.com/ben-manes/gradle-versions-plugin
tasks.withType<DependencyUpdatesTask> {
    checkConstraints = true
}