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
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

@Suppress("unused") // Suppress warning because it will be called by gradle.
class BuildConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create(EXTENSION_NAME, BuildConfigExtension::class.java)

        project.afterEvaluate { proj ->
            val genBuildConfigTask = proj.tasks.register(TASK_NAME, GenBuildConfigTask::class.java).get()

            val pluginType = when {
                proj.plugins.hasPlugin(PluginType.KOTLIN.pluginId) -> PluginType.KOTLIN
                proj.plugins.hasPlugin(PluginType.JAVA.pluginId) -> PluginType.JAVA
                else -> throw IllegalStateException(
                    "'io.woong.buildconfig' plugin requires '${PluginType.JAVA.pluginId}' " +
                    "or '${PluginType.KOTLIN.pluginId}' plugin."
                )
            }

            proj.configureSourceSets(pluginType)
            proj.configureTaskDependency(pluginType, genBuildConfigTask)
        }
    }

    private fun Project.configureSourceSets(pluginType: PluginType) {
        val buildDir = this.buildDir.toString()

        when (pluginType) {
            PluginType.JAVA -> {
                val ext = this.extensions.findByType(JavaPluginExtension::class.java)
                    ?: throw IllegalStateException("Cannot found java plugin extension.")

                ext.sourceSets.configureEach { sourceSet ->
                    sourceSet.java.srcDir("$buildDir/generated/sources/buildconfig/java/main")
                }
            }
            PluginType.KOTLIN -> {
                val ext = this.extensions.findByType(KotlinJvmProjectExtension::class.java)
                    ?: throw IllegalStateException("Cannot found kotlin plugin extension.")

                ext.sourceSets.configureEach { sourceSet ->
                    sourceSet.kotlin.srcDir("$buildDir/generated/sources/buildconfig/kotlin/main")
                }
            }
        }
    }

    private fun Project.configureTaskDependency(pluginType: PluginType, task: GenBuildConfigTask) {
        val compileTaskType = when (pluginType) {
            PluginType.JAVA -> JavaCompile::class.java
            PluginType.KOTLIN -> KotlinCompile::class.java
        }

        this.tasks
            .withType(compileTaskType)
            .configureEach { it.dependsOn(task) }
    }

    private enum class PluginType(val pluginId: String) {
        JAVA("java"),
        KOTLIN("org.jetbrains.kotlin.jvm")
    }

    companion object {
        const val EXTENSION_NAME: String = "buildConfig"
        const val TASK_NAME: String = "genBuildConfig"
    }
}

