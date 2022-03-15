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

import com.squareup.javapoet.ClassName
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ClassGeneratingTest {
    @TempDir
    private lateinit var testProjectDir: File
    private lateinit var buildGradleFile: File
    private lateinit var settingsGradleFile: File

    private val testPackageName: String = "io.woong.buildconfig.test"

    @BeforeEach
    fun setup() {
        settingsGradleFile = File(testProjectDir, "settings.gradle")
        settingsGradleFile.writeText(
            """
                pluginManagement {
                    repositories {
                        mavenCentral()
                        gradlePluginPortal()
                    }
                }
                rootProject.name = "buildconfig-test"
            """.trimIndent()
        )

        buildGradleFile = File(testProjectDir, "build.gradle")
    }

    @Test
    fun `test default class and package name`() {
        buildGradleFile.writeText(
            """
                plugins {
                    id "java"
                    id "io.woong.buildconfig" version "0.1.1"
                }
                
                group "$testPackageName"
                version "1.0.0"
                
                repositories {
                    mavenCentral()
                }
                
                buildConfig {}
            """.trimIndent()
        )

        GradleRunner
            .create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("genBuildConfig", "--stacktrace")
            .build()

        Assertions.assertEquals(
            "BuildConfig",
            ClassName.get(testPackageName, "BuildConfig").simpleName()
        )

        Assertions.assertEquals(
            "$testPackageName.BuildConfig",
            ClassName.get(testPackageName, "BuildConfig").canonicalName()
        )
    }

    @Test
    fun `test custom package name`() {
        val customPackageName = "$testPackageName.build"

        buildGradleFile.writeText(
            """
                plugins {
                    id "java"
                    id "io.woong.buildconfig" version "0.1.1"
                }
                
                group "$testPackageName"
                version "1.0.0"
                
                repositories {
                    mavenCentral()
                }
                
                buildConfig {
                    packageName = "$customPackageName"
                }
            """.trimIndent()
        )

        GradleRunner
            .create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("genBuildConfig", "--stacktrace")
            .build()

        Assertions.assertEquals(
            "BuildConfig",
            ClassName.get(customPackageName, "BuildConfig").simpleName()
        )

        Assertions.assertEquals(
            "$customPackageName.BuildConfig",
            ClassName.get(customPackageName, "BuildConfig").canonicalName()
        )
    }

    @Test
    fun `test custom class name`() {
        val customClassName = "BuildMetadata"

        buildGradleFile.writeText(
            """
                plugins {
                    id "java"
                    id "io.woong.buildconfig" version "0.1.1"
                }
                
                group "$testPackageName"
                version "1.0.0"
                
                repositories {
                    mavenCentral()
                }
                
                buildConfig {
                    className = "$customClassName"
                }
            """.trimIndent()
        )

        GradleRunner
            .create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("genBuildConfig", "--stacktrace")
            .build()

        Assertions.assertEquals(
            customClassName,
            ClassName.get(testPackageName, customClassName).simpleName()
        )

        Assertions.assertEquals(
            "$testPackageName.$customClassName",
            ClassName.get(testPackageName, customClassName).canonicalName()
        )
    }
}
