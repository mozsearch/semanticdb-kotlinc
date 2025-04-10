import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import groovy.lang.Closure
import org.gradle.jvm.toolchain.internal.CurrentJvmToolchainSpec

plugins {
    kotlin("jvm") version "2.1.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.diffplug.spotless") version "5.17.1"
}

allprojects {
    if (name !in setOf("minimized", "semanticdb-kotlin")) {
        apply(plugin = "com.diffplug.spotless")
        spotless {
            kotlin {
                ktfmt().dropboxStyle()
            }
        }
    }
}

repositories {
    mavenCentral()
}

allprojects {
    afterEvaluate {
        tasks.withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }

        kotlin {
            jvmToolchain {
                (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(8))
            }
        }

        tasks.withType<JavaCompile> {
            sourceCompatibility = "1.8"
        }
    }
}
