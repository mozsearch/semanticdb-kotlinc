plugins {
    kotlin("jvm") version "2.3.0"
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
