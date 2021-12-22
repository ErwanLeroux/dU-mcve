import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.VersionsPlugin

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.github.ben-manes:gradle-versions-plugin:0.39.0")
    }
}

apply(plugin = "VersionsPlugin")

// pour rejeter, il faut retourner true
//noinspection GroovyAssignabilityCheck
fun specific(candidate: ModuleComponentIdentifier): Boolean {
    // Cette version date de novembre 2005 et n"est pas ignoré par le plugin
    if (candidate.module == "commons-io" && candidate.version == "20030203.000550") {
        return true
    }
    val upperCase: String = candidate.version.toUpperCase()
    // On exclut les versions du type 2.4.0-b180830.0359
    // Les prochaines versions sont dans jakarta.xml.bind:jakarta.xml.bind-api, ce qui nécessite une grosse migration pour un gain incertain
    if (candidate.group == "javax.xml.bind" && candidate.module == "jaxb-api" && upperCase.contains("-B")) {
        return true
    }
    // On exclut les versions 7.x tant que la version 6 du framemwork spring 6 n"est pas sorti : https://github.com/spring-projects/spring-framework/issues/25354
    if (candidate.group == "org.hibernate.validator" && candidate.module == "hibernate-validator" && upperCase.startsWith(
            "7."
        )
    ) {
        return true
    }
    // On exclut les versions 4.x tant que la version 3 du framemwork spring boot n"est pas disponible
    if (candidate.group == "org.glassfish" && candidate.module == "jakarta.el" && upperCase.startsWith("4.")) {
        return true
    }
    // On exclut les versions 2.13 tant que la migration et les impacts ne sont pas bien analysés
    if (candidate.group.startsWith("com.fasterxml.jackson") && upperCase.startsWith("2.13")) {
        return true
    }
    return false
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE:FINAL:GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

configure<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version) || specific(candidate)
    }
    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Release candidate")
                }
            }
        }
    }
    checkConstraints = true
}