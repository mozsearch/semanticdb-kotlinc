import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.shadow)
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly(kotlin("compiler-embeddable"))
    implementation(libs.protobuf.java)

    testImplementation(kotlin("compiler-embeddable"))
    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kctfork.core)
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    plugins {
        kotlin { }
    }
}

tasks.processResources {
    dependsOn(tasks.named("generateProto"))
}

tasks.compileKotlin {
    dependsOn(tasks.named("generateProto"))
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

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenShadow") {
            from(components["shadow"])
        }
    }
    repositories {
        maven {
            group = "com.github.mozsearch"
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}

