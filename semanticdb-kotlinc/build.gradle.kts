import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly(kotlin("compiler-embeddable"))
    implementation("com.google.protobuf:protobuf-java:4.33.1")
    implementation(projects.semanticdbKotlin)

    testImplementation(kotlin("compiler-embeddable"))
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core:6.0.5")
    testImplementation("dev.zacsweers.kctfork:core:0.11.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xinline-classes",
            "-Xcontext-parameters",
        )
    }
}

tasks.jar {
    archiveClassifier.set("slim")
    manifest {
        attributes["Specification-Title"] = project.name
        attributes["Specification-Version"] = project.version
        attributes["Implementation-Title"] = "semanticdb-kotlinc"
        attributes["Implementation-Version"] = project.version
    }
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
