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
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.io.File

@Suppress("unused") // Suppress warning because it will be called by gradle.
class BuildConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val buildConfigExtension = project.addExtension()

        project.afterEvaluate { p ->
            val pluginType = when {
                p.plugins.hasPlugin(PluginType.JAVA.pluginId) -> PluginType.JAVA
                p.plugins.hasPlugin(PluginType.KOTLIN.pluginId) -> PluginType.KOTLIN
                else -> throw IllegalStateException(
                    "'io.woong.buildconfig' plugin requires '${PluginType.JAVA.pluginId}' " +
                            "or '${PluginType.KOTLIN.pluginId}' plugin."
                )
            }

            val genBuildConfigTask = p.registerAndConfigureTask(buildConfigExtension)
            p.configureSourceSets(pluginType)
            p.configureTaskDependency(pluginType, genBuildConfigTask)
        }
    }

    private fun Project.addExtension(): BuildConfigExtensionImpl {
        val extension = BuildConfigExtensionImpl()
        this.extensions.add(BuildConfigExtension::class.java, EXTENSION_NAME, extension)
        return extension
    }

    private fun Project.registerAndConfigureTask(extension: BuildConfigExtensionImpl): TaskProvider<GenBuildConfigTask> {
        val task = this.tasks.register(TASK_NAME, GenBuildConfigTask::class.java) { t ->
            t.packageName = extension.packageName.ifBlank { this.group.toString() }
            t.className = extension.className.ifBlank { DEFAULT_CLASS_NAME }
            t.outputDir = File("${project.buildDir}/generated/source/buildconfig/java/main")
        }
        return task
    }

    private fun Project.configureSourceSets(pluginType: PluginType) {
        val buildDir = this.buildDir.toString()

        when (pluginType) {
            PluginType.JAVA -> {
                val ext = this.extensions.findByType(JavaPluginExtension::class.java)
                    ?: throw IllegalStateException("Cannot found java plugin extension.")

                ext.sourceSets.configureEach { sourceSet ->
                    sourceSet.java.srcDir("$buildDir/generated/source/buildconfig/java/main")
                }
            }
            PluginType.KOTLIN -> {
                val ext = this.extensions.findByType(KotlinJvmProjectExtension::class.java)
                    ?: throw IllegalStateException("Cannot found kotlin plugin extension.")

                ext.sourceSets.configureEach { sourceSet ->
                    sourceSet.kotlin.srcDir("$buildDir/generated/source/buildconfig/kotlin/main")
                }
            }
        }
    }

    private fun Project.configureTaskDependency(pluginType: PluginType, task: TaskProvider<GenBuildConfigTask>) {
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
        const val DEFAULT_CLASS_NAME: String = "BuildConfig"
    }
}

