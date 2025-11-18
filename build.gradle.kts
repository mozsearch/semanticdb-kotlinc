plugins {
    kotlin("jvm") version "2.2.20"
}

repositories {
    mavenCentral()
}

allprojects {
    afterEvaluate {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }
    }
}
