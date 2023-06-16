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
        description = "Gradle plugin which generates static fields on build time likes Android BuildConfig for JVM gradle project."
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

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
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
