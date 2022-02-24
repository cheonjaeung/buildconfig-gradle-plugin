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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

@Suppress("unused") // Suppress warning because it will be called by gradle.
class BuildConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("buildConfig", BuildConfigExtension::class.java)

        project.afterEvaluate { proj ->
            val genBuildConfigTask = proj.tasks.register("genBuildConfig", GenBuildConfigTask::class.java)

            when {
                proj.plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> {
                    println("kotlin jvm plugin detected.")

                    val kotlinPluginExtension = proj.extensions.findByType(KotlinJvmProjectExtension::class.java)
                        ?: throw IllegalStateException("Cannot found kotlin gradle plugin extension.")

                    kotlinPluginExtension.sourceSets.configureEach { sourceSet ->
                        sourceSet.kotlin.srcDir("${project.buildDir}/generated/sources/buildConfig/kotlin/main")
                    }

                    proj.tasks.withType(KotlinCompile::class.java).configureEach {
                        it.dependsOn(genBuildConfigTask)
                    }
                }

                proj.plugins.hasPlugin(JavaPlugin::class.java) -> {
                    println("java plugin detected.")

                    val javaPluginExtension = proj.extensions.findByType(JavaPluginExtension::class.java)
                        ?: throw IllegalStateException("Cannot found java gradle plugin extension.")

                    val mainSrcSet = javaPluginExtension.sourceSets.findByName("main")
                        ?: throw IllegalStateException("Cannot found main source set.")

                    mainSrcSet.java.srcDir("${project.buildDir}/generated/sources/buildConfig/java/main")

                    proj.tasks.withType(JavaCompile::class.java).configureEach {
                        it.dependsOn(genBuildConfigTask)
                    }
                }

                else -> throw IllegalStateException(
                    "'io.woong.buildconfig' plugin requires 'java' or 'org.jetbrains.kotlin.jvm' plugin."
                )
            }
        }
    }
}

