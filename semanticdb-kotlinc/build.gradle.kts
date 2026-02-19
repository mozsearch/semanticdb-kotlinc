import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly(kotlin("compiler-embeddable"))
    implementation(libs.protobuf.java)
    implementation(projects.semanticdbKotlin)

    testImplementation(kotlin("compiler-embeddable"))
    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kctfork.core)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xinline-classes",
            "-Xcontext-parameters",
        )
    }
}

val semanticdbJar: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    add(semanticdbJar.name, tasks.shadowJar)
}

tasks.jar {
    archiveClassifier = "slim"
    manifest {
        attributes["Specification-Title"] = project.name
        attributes["Specification-Version"] = project.version
        attributes["Implementation-Title"] = "semanticdb-kotlinc"
        attributes["Implementation-Version"] = project.version
    }
}

tasks.shadowJar {
    archiveClassifier = ""
    relocate("com.intellij", "org.jetbrains.kotlin.com.intellij")
    minimize()
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
        events("passed", "failed")
    }
    maxHeapSize = "2g"
}
