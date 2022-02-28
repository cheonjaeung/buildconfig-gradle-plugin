import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "1.6.10"
    id("com.vanniktech.maven.publish") version "0.15.1"
}

group = "io.woong.buildconfig"
version = "0.1.1"

gradlePlugin {
    plugins.create("buildConfig") {
        id = "io.woong.buildconfig"
        displayName = "Build Config Plugin"
        description = "Gradle plugin that generates static fields on build time likes Android BuildConfig but for JVM."
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

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

plugins.withId("com.vanniktech.maven.publish") {
    mavenPublish {
        sonatypeHost = com.vanniktech.maven.publish.SonatypeHost.S01
    }
}
