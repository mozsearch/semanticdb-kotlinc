import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm")
    id("com.google.protobuf") version "0.9.4"
}

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.google.protobuf:protobuf-java:3.17.3")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.protobuf:protobuf-java:3.17.3")
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
        artifact = "com.google.protobuf:protoc:3.17.3"
    }

    plugins {
        kotlin { }
    }

}
