rootProject.name = "lsif-kotlin"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "semanticdb-kotlin",
    "semanticdb-kotlinc",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
