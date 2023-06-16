# BuildConfig Gradle Plugin

[![ci](https://github.com/cheonjaewoong/buildconfig-gradle-plugin/actions/workflows/ci.yml/badge.svg)](https://github.com/cheonjaewoong/buildconfig-gradle-plugin/actions/workflows/ci.yml)
![minimum jdk version](https://img.shields.io/badge/jdk-1.8%2B-007396?logo=java&logoColor=ff7800)
[![maven central](https://img.shields.io/maven-central/v/io.woong.buildconfig/buildconfig-gradle-plugin?label=maven%20central&logo=apache-maven&logoColor=red)](https://search.maven.org/artifact/io.woong.buildconfig/buildconfig-gradle-plugin)

BuildConfig Gradle Plugin is a Gradle plugin for generating static fields from Gradle buildscript.

In Android, there is a feature called `BuildConfig`.
Android Gradle Plugin generates `BuildConfig` class on build time that contains some static fields likes application id, version name and user defined values.
It is very helpful for using build-related data in android source code.
But, there is no feature in normal JVM project.
This project's main goal is offering BuildConfig-like features in JVM gradle project.

## Getting Started

### Setup

This plugin requires JDK 8 or above.

BuildConfig Gradle Plugin is published on Maven Central.
To use this plugin, set `mavenCentral` repository in `pluginManagement` or `buildscript` block.

**Using `pluginManagement` block:**

```groovy
pluginManagement {
    repositories {
        mavenCentral()
    }
}
```

**Using `buildscript` block:**

```groovy
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath "io.woong.buildconfig:buildconfig-gradle-plugin:x.y.z"
    }
}
```

### Applying plugin

Apply plugin to where you want to configure build config.
This plugin depends on `java` or `org.jetbrains.kotlin.jvm` plugin.
Before applying this plugin, set `java`, `java-library` or `org.jetbrains.kotlin.jvm` plugin.

**Using `apply` method:**

```groovy
apply plugin: "java"
apply plugin: "io.woong.buildconfig"
```

**Using plugin DSL:**

```groovy
plugins {
    java
    id "io.woong.buildconfig" version "x.y.z"
}
```

### Setting Static Fields

BuildConfig Gradle Plugin provides an extension named `buildConfig`.
In `buildConfig` scope, you can define static fields and class configurations.

```groovy
buildConfig {
    field("APP_NAME", "SampleApp")                    // String field.
    field("VERSION_CODE", 12)                         // Int field.
    field("VERSION_NAME", "1.5.0")                    // String field.
    field("API_KEY", System.getenv("SAMPLE_API_KEY")) // String field from system env variable.
}
```

### Generating Static Fields

It offers a gradle task, `genBuildConfig`.
Run this task to generate static class.

```shell
./gradlew genBuildConfig
```

After executing gradle task, You can access generated class in source code like below:

```java
public class BuildConfigSample {
    public static void main(String[] args) {
        String appName = BuildConfig.APP_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        String apiKey = BuildConfig.API_KEY;
    }
}
```

## Configuration

### `field`

You can set primitives and `String` type fields.

```groovy
buildConfig {
    field("BYTE_FIELD", (byte) 12)
    field("SHORT_FIELD", (short) 400)
    field("INT_FIELD", 20000)
    field("LONG_FIELD", (long) 21000000000L)
    field("FLOAT_FIELD", 1601.5f)
    field("DOUBLE_FIELD", 12131.0)
    field("CHAR_FIELD", 'c')
    field("STRING_FIELD", "this is string sample")
}
```

### `packageName` and `className`

Set `packageName` and `className` property in `buildConfig` block.

```groovy
buildConfig {
    packageName = "com.example.app.build" // Set custom package name.
    className = "BuildMetadata"           // Set custom class name.
    field("APP_NAME", "SampleApp")
    field("VERSION_CODE", 12)
    field("VERSION_NAME", "1.5.0")
    field("API_KEY", System.getenv("SAMPLE_API_KEY"))
}
```

Then, the `BuildMetadata` class will be generated in `com.example.app.build` package.

```java
package com.example.app;

import com.example.app.build.BuildMetadata;

public class BuildConfigSample {
    public static void main(String[] args) {
        String appName = BuildMetadata.APP_NAME;
        int versionCode = BuildMetadata.VERSION_CODE;
        String versionName = BuildMetadata.VERSION_NAME;
        String apiKey = BuildMetadata.API_KEY;
    }
}
```

## License

Copyright 2022 Jaewoong Cheon. All rights reserved.

Licensed under the Apache License, Version 2.0

See [license file](./LICENSE.txt) for more detail.
