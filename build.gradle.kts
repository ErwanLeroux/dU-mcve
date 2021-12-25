plugins {
    java
    id("com.github.ben-manes.versions") version "0.39.0"
    id("my-plugin-log4j")
//    id("fr.elendar.log4jConvention")
}

// apply(from = "gradle/checkUpdateVersion.gradle.kts")

repositories {
    mavenLocal()
    mavenCentral()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    checkConstraints = true
}
