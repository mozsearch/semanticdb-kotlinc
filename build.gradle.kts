plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
}

subprojects {
    afterEvaluate {
        java {
            toolchain {
                languageVersion = JavaLanguageVersion.of(17)
            }
        }
    }
}
