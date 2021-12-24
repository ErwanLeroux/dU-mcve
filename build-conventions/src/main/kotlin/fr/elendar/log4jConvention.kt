package fr.elendar

import mu.KotlinLogging
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyConstraint
import org.gradle.api.artifacts.MutableVersionConstraint
import org.gradle.api.artifacts.dsl.DependencyConstraintHandler

@Suppress("unused")
class Log4jConvention : Plugin<Project> {
    private val log = KotlinLogging.logger { }
    private val dependencies = listOf(
        "org.apache.logging.log4j:log4j-core",
        "org.apache.logging.log4j:log4j-slf4j-impl",
        "org.apache.logging.log4j:log4j-jul"
    )

    override fun apply(project: Project) {
        project.dependencies.constraints(fun(constraints: DependencyConstraintHandler) {
            listOf("compileOnly", "compileClasspath", "implementation", "runtimeOnly", "runtimeClasspath").forEach(fun(configuration: String) {
                dependencies.forEach(fun(dependency: String) {
                    constraints.add(configuration, dependency, fun(c: DependencyConstraint) {
                        c.version(fun(v: MutableVersionConstraint) {
                            v.strictly("[2.17, 3[")
                            v.require("2.17.0")
                            v.reject("[2.0, 2.17[")
                        })
                        c.because("CVE-2021-44228,CVE-2021-45046,CVE-2021-45105: Log4j vulnerable to remote code execution")
                        log.info { "Applied log4j version constraint to $dependency for configuration $configuration" }
                    })
                })
            })
        })
        log.info { "Applied log4j version constraints" }
    }
}