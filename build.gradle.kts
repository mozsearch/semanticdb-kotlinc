import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.2.20"
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
