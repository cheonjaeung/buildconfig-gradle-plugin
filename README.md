# BuildConfig Gradle Plugin

_A gradle plugin to generate static fields on build time._

[![ci](https://github.com/cheonjaewoong/buildconfig-gradle-plugin/actions/workflows/ci.yml/badge.svg)](https://github.com/cheonjaewoong/buildconfig-gradle-plugin/actions/workflows/ci.yml)
![JDK Minimum](https://img.shields.io/badge/jdk-1.8%2B-007396?logo=java&logoColor=ff7800)
[![Maven Central](https://img.shields.io/maven-central/v/io.woong.buildconfig/buildconfig-gradle-plugin?label=maven%20central&logo=apache-maven&logoColor=red)](https://search.maven.org/artifact/io.woong.buildconfig/buildconfig-gradle-plugin)

There is a feature called `BuildConfig` in Android project.
Android gradle plugin generates `BuildConfig` class that contains some static fields.
For instance, application id, version and some user custom fields can be defined in `BuildConfig`.
It is useful to define some build metadata or secret keys (like API key).

In Android project, it is simple to use and useful.
But in simple JVM project, it is not.
This is why I create this project.

## Usage

1. Setup gradle plugin classpath to buildscript in top-level `build.gradle` file.

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
        
    dependencies {
        classpath "io.woong.buildconfig:buildconfig-gradle-plugin:0.1.1"
    }
}
```    

2. Apply plugin to where you want to configure build config.

```groovy
apply plugin: "java"
apply plugin: "io.woong.buildconfig"
```

3. Define some static fields using `field()` method in the `buildConfig` block.

```groovy
apply plugin: "io.woong.buildconfig"
   
buildConfig {
    field("APP_NAME", "SampleApp")                    // String field.
    field("VERSION_CODE", 12)                         // Int field.
    field("VERSION_NAME", "1.5.0")                    // String field.
    field("API_KEY", System.getenv("SAMPLE_API_KEY")) // String field from system env variable.
}
```

4. Run `build` or `genBuildConfig` task to generate static fields.

```shell
./gradlew build
```

or

```shell
./gradlew genBuildConfig
```

5. Use generated fields in your code. The `BuildConfig` class will be generated in your project's group package.

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
    field("CHAR_FIELD", "c") // Char type will be generated as String.
    field("STRING_FIELD", "this is sample")
}
```

## License

```
Copyright 2022 Jaewoong Cheon

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
