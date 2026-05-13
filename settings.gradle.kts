rootProject.name = "semanticdb-kotlinc-plugin"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "semanticdb-kotlinc",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
