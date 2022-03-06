/*
 * Copyright 2022 Jaewoong Cheon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.woong.buildconfig

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class PluginDependencyTest {
    @TempDir
    private lateinit var testProjectDir: File
    private lateinit var buildGradleFile: File
    private lateinit var settingsGradleFile: File

    @BeforeEach
    fun setup() {
        settingsGradleFile = File(testProjectDir, "settings.gradle")
        buildGradleFile = File(testProjectDir, "build.gradle")
    }

    @Test
    fun `test is build config plugin cannot run as standalone`() {
        settingsGradleFile.writeText(
            """
                pluginManagement {
                    repositories {
                        mavenCentral()
                        gradlePluginPortal()
                    }
                }
                rootProject.name = "groovy-gradle-test"
            """.trimIndent()
        )

        buildGradleFile.writeText(
            """
                plugins {
                    id "io.woong.buildconfig" version "0.1.1"
                }
                
                group "io.woong.test.groovy"
                version "1.0.0"
                
                repositories {
                    mavenCentral()
                }
                
                buildConfig {
                    field("APP_VERSION_CODE", 1)
                    field("APP_VERSION_NAME", "1.0.0")
                }
            """.trimIndent()
        )

        val result = GradleRunner
            .create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("build", "--stacktrace")
            .buildAndFail()

        Assertions.assertTrue(result.output.contains("BUILD FAILED"))
    }

    @Test
    fun `test is build config plugin can run with java plugin`() {
        settingsGradleFile.writeText(
            """
                pluginManagement {
                    repositories {
                        mavenCentral()
                        gradlePluginPortal()
                    }
                }
                rootProject.name = "groovy-gradle-test"
            """.trimIndent()
        )

        buildGradleFile.writeText(
            """
                plugins {
                    id "java"
                    id "io.woong.buildconfig" version "0.1.1"
                }
                
                group "io.woong.test.groovy"
                version "1.0.0"
                
                repositories {
                    mavenCentral()
                }
                
                buildConfig {
                    field("APP_VERSION_CODE", 1)
                    field("APP_VERSION_NAME", "1.0.0")
                }
            """.trimIndent()
        )

        val result = GradleRunner
            .create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("build", "--stacktrace")
            .build()

        Assertions.assertTrue(result.output.contains("BUILD SUCCESSFUL"))
    }

    @Test
    fun `test is build config plugin can run with java-library plugin`() {
        settingsGradleFile.writeText(
            """
                pluginManagement {
                    repositories {
                        mavenCentral()
                        gradlePluginPortal()
                    }
                }
                rootProject.name = "groovy-gradle-test"
            """.trimIndent()
        )

        buildGradleFile.writeText(
            """
                plugins {
                    id "java-library"
                    id "io.woong.buildconfig" version "0.1.1"
                }
                
                group "io.woong.test.groovy"
                version "1.0.0"
                
                repositories {
                    mavenCentral()
                }
                
                buildConfig {
                    field("APP_VERSION_CODE", 1)
                    field("APP_VERSION_NAME", "1.0.0")
                }
            """.trimIndent()
        )

        val result = GradleRunner
            .create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("build", "--stacktrace")
            .build()

        Assertions.assertTrue(result.output.contains("BUILD SUCCESSFUL"))
    }

    @Test
    fun `test is build config plugin can run with kotlin plugin`() {
        settingsGradleFile.writeText(
            """
                pluginManagement {
                    repositories {
                        mavenCentral()
                        gradlePluginPortal()
                    }
                }
                rootProject.name = "groovy-gradle-test"
            """.trimIndent()
        )

        buildGradleFile.writeText(
            """
                plugins {
                    id "org.jetbrains.kotlin.jvm" version "1.6.10"
                    id "io.woong.buildconfig" version "0.1.1"
                }
                
                group "io.woong.test.groovy"
                version "1.0.0"
                
                repositories {
                    mavenCentral()
                }
                
                buildConfig {
                    field("APP_VERSION_CODE", 1)
                    field("APP_VERSION_NAME", "1.0.0")
                }
            """.trimIndent()
        )

        val result = GradleRunner
            .create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("build", "--stacktrace")
            .build()

        Assertions.assertTrue(result.output.contains("BUILD SUCCESSFUL"))
    }
}
