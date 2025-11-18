plugins {
    kotlin("jvm")
    id("com.google.protobuf") version "0.9.5"
}

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.google.protobuf:protobuf-java:4.33.1")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.protobuf:protobuf-java:4.33.1")
}


afterEvaluate {
    tasks.processResources {
        dependsOn(tasks.getByName("generateProto"))
    }

    tasks.compileKotlin {
        dependsOn(tasks.getByName("generateProto"))
    }
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.33.1"
    }

    plugins {
        kotlin { }
    }

}
