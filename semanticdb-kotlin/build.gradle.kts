plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.protobuf)
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.protobuf.java)
}

tasks.processResources {
    dependsOn(tasks.named("generateProto"))
}

tasks.compileKotlin {
    dependsOn(tasks.named("generateProto"))
}


protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }

    plugins {
        kotlin { }
    }

}
