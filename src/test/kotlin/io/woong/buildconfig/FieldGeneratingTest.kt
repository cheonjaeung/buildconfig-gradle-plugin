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

class FieldGeneratingTest {
    @TempDir
    private lateinit var testProjectDir: File
    private lateinit var buildGradleFile: File
    private lateinit var settingsGradleFile: File
    private lateinit var sourceCodeFile: File

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

        val sourceDirectory = File("$testProjectDir/src/main/java/io/woong/testapp")
        sourceDirectory.mkdirs()
        sourceCodeFile = File("$testProjectDir/src/main/java/io/woong/testapp", "App.java")
    }

    @Test
    fun `test primitive type field generating`() {
        val byteField: Byte = 12
        val shortField: Short = 60
        val intField = 2000
        val longField = 21000000000L
        val floatField = 1555.5f
        val doubleField = 1910181.7
        val charField = 'c'
        val stringField = "this is sample string"

        buildGradleFile.writeText(
            """
                plugins {
                    id "application"
                    id "java"
                    id "io.woong.buildconfig" version "0.1.1"
                }
                
                group "io.woong.buildconfig.test"
                version "1.0.0"
                
                application {
                    mainClassName = "io.woong.buildconfig.test.App"
                }
                
                repositories {
                    mavenCentral()
                }
                
                buildConfig {
                    field("BYTE_FIELD", (byte) $byteField)
                    field("SHORT_FIELD", (short) $shortField)
                    field("INT_FIELD", $intField)
                    field("LONG_FIELD", (long) $longField)
                    field("FLOAT_FIELD", $floatField)
                    field("DOUBLE_FIELD", $doubleField)
                    field("CHAR_FIELD", "$charField")
                    field("STRING_FIELD", "$stringField")
                }
            """.trimIndent()
        )

        sourceCodeFile.writeText(
            """
                package io.woong.buildconfig.test;
                
                public class App {
                    public static void main(String[] args) {
                        System.out.println("BYTE_FIELD: " + BuildConfig.BYTE_FIELD);
                        System.out.println("SHORT_FIELD: " + BuildConfig.SHORT_FIELD);
                        System.out.println("INT_FIELD: " + BuildConfig.INT_FIELD);
                        System.out.println("LONG_FIELD: " + BuildConfig.LONG_FIELD);
                        System.out.println("FLOAT_FIELD: " + BuildConfig.FLOAT_FIELD);
                        System.out.println("DOUBLE_FIELD: " + BuildConfig.DOUBLE_FIELD);
                        System.out.println("CHAR_FIELD: " + BuildConfig.CHAR_FIELD);
                        System.out.println("STRING_FIELD: " + BuildConfig.STRING_FIELD);
                    }
                }
            """.trimIndent()
        )

        val runResult = GradleRunner
            .create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("run", "--stacktrace")
            .build()

        Assertions.assertTrue(runResult.output.contains("BUILD SUCCESSFUL"))

        Assertions.assertTrue(runResult.output.contains("BYTE_FIELD: $byteField"))
        Assertions.assertTrue(runResult.output.contains("SHORT_FIELD: $shortField"))
        Assertions.assertTrue(runResult.output.contains("INT_FIELD: $intField"))
        Assertions.assertTrue(runResult.output.contains("LONG_FIELD: $longField"))
        Assertions.assertTrue(runResult.output.contains("FLOAT_FIELD: $floatField"))
        Assertions.assertTrue(runResult.output.contains("DOUBLE_FIELD: $doubleField"))
        Assertions.assertTrue(runResult.output.contains("CHAR_FIELD: $charField"))
        Assertions.assertTrue(runResult.output.contains("STRING_FIELD: $stringField"))
    }
}
