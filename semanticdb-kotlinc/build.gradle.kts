import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly(kotlin("compiler-embeddable"))
    implementation("com.google.protobuf", "protobuf-java", "3.15.7")
    implementation(projects.semanticdbKotlin)

    testImplementation(kotlin("compiler-embeddable"))
    testImplementation(kotlin("test"))
    testImplementation("io.kotest", "kotest-assertions-core", "4.6.3")

    // Unable to use com.github.tschuchortdev:kotlin-compile-testing until 1.9.x support is fixed
    //   https://github.com/tschuchortdev/kotlin-compile-testing/issues/390
    // Until then, we use the fork from https://github.com/ZacSweers/kotlin-compile-testing instead.
    // testImplementation("com.github.tschuchortdev", "kotlin-compile-testing", "1.5.0")
    testImplementation("dev.zacsweers.kctfork", "core", "0.7.1")
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

val sourceJar = task<Jar>("sourceJar") {
    dependsOn(tasks.classes)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar = task<Jar>("javadocJar") {
    dependsOn(tasks.javadoc)
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
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
