plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "1.6.10"
    id("maven-publish") // For publish to maven local to test
}

group = "io.woong.buildconfig"
version = "0.1.0"

gradlePlugin {
    plugins.create("buildConfig") {
        id = "io.woong.buildconfig"
        implementationClass = "io.woong.buildconfig.BuildConfigPlugin"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin"))
    implementation("com.squareup:javapoet:1.13.0")
}
