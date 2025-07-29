import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.2.20-RC2"
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
        kotlin {
            compilerOptions {
                jvmTarget = JvmTarget.JVM_1_8
            }
            jvmToolchain {
                (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(8))
            }
        }

        tasks.withType<JavaCompile> {
            sourceCompatibility = "1.8"
        }
    }
}
