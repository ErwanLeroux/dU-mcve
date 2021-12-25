package fr.elendar

import mu.KotlinLogging
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.*
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier
import org.gradle.api.internal.artifacts.dependencies.DefaultDependencyConstraint
import org.gradle.api.internal.artifacts.dependencies.DefaultImmutableVersionConstraint

@Suppress("unused")
class Log4jConvention : Plugin<Project> {
    private val log = KotlinLogging.logger { }
    private val artifacts = listOf(
        "org.apache.logging.log4j:log4j-core",
        "org.apache.logging.log4j:log4j-slf4j-impl",
        "org.apache.logging.log4j:log4j-jul"
    )

    override fun apply(project: Project) {
        //(project.dependencies as org.gradle.api.internal.artifacts.configurations.DefaultConfigurationContainer).toTypedArray()
        project.configurations.forEach(fun(configuration: Configuration) {
            configuration.dependencies.forEach(fun(dependency: Dependency) {
                if (dependency.group == "org.apache.logging.log4j") {
//                    public DefaultMutableVersionConstraint(VersionConstraint versionConstraint) {
//                        this(versionConstraint.getPreferredVersion(), versionConstraint.getRequiredVersion(), versionConstraint.getStrictVersion(), versionConstraint.getRejectedVersions());
//                    }
                    val versionConstraint: VersionConstraint =
                        DefaultImmutableVersionConstraint("2.17.0", "2.17.0", "[2.17, 3[", listOf("[2.0, 2.17["), null)
                    val moduleId: ModuleIdentifier = DefaultModuleIdentifier.newId(dependency.group, dependency.name)

                    val constraint: DependencyConstraint = DefaultDependencyConstraint(moduleId, versionConstraint)
                    constraint.because("CVE-2021-44228,CVE-2021-45046,CVE-2021-45105: Log4j vulnerable to remote code execution")
                    log.info { "Applied log4j version constraint to $dependency for configuration ${configuration.name}" }
                    configuration.dependencyConstraints.add(constraint)
//                    configuration.dependencyConstraints.add(fun(c: DependencyConstraint) {
//                        c.version(fun(v: MutableVersionConstraint) {
//                            v.strictly("[2.17, 3[")
//                            v.require("2.17.0")
//                            v.reject("[2.0, 2.17[")
//                        })
//                        c.because("CVE-2021-44228,CVE-2021-45046,CVE-2021-45105: Log4j vulnerable to remote code execution")
//                    })
                }
            })

        })
        log.info { "Applied log4j version constraints" }
    }
}