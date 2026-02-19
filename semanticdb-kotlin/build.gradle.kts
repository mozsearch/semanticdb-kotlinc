plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.protobuf)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.protobuf.java)
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
        artifact = libs.protobuf.protoc.get().toString()
    }

    plugins {
        kotlin { }
    }

}
