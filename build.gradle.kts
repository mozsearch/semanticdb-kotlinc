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
        extensions.findByType<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension>()
            ?.compilerOptions
            ?.allWarningsAsErrors
            ?.set(true)
    }
}
