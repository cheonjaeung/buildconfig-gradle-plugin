import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "1.6.10"
    id("com.vanniktech.maven.publish") version "0.15.1"
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

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

plugins.withId("com.vanniktech.maven.publish") {
    mavenPublish {
        sonatypeHost = com.vanniktech.maven.publish.SonatypeHost.S01
    }
}
