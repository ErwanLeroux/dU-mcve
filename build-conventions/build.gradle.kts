// https://github.com/gradle/kotlin-dsl-samples/tree/master/samples/buildSrc-plugin
plugins {
    `kotlin-dsl`
}

dependencies {
    // https://github.com/MicroUtils/kotlin-logging
    implementation ("io.github.microutils:kotlin-logging-jvm:2.1.21")
}


// https://stackoverflow.com/questions/70326473/gradle-dependency-constrains-are-not-applied/70358360
gradlePlugin {
    plugins {
        create("myPlugins") {
            id = "my-plugin-log4j"
            implementationClass = "fr.elendar.Log4jConvention"
        }
    }
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}
