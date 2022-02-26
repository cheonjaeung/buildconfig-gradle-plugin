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
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import java.io.File

@Suppress("unused") // Suppress warning because it will be called by gradle.
class BuildConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val buildConfigExtension = project.addExtension()

        project.afterEvaluate { p ->
            val genBuildConfigTask = p.registerAndConfigureTask(buildConfigExtension)
            p.configureTaskDependency(genBuildConfigTask)
            p.configureSourceSets()
        }
    }

    private fun Project.addExtension(): BuildConfigExtensionImpl {
        val extension = BuildConfigExtensionImpl()
        this.extensions.add(BuildConfigExtension::class.java, EXTENSION_NAME, extension)
        return extension
    }

    private fun Project.registerAndConfigureTask(extension: BuildConfigExtensionImpl): TaskProvider<GenBuildConfigTask> {
        return this.tasks.register(TASK_NAME, GenBuildConfigTask::class.java) { t ->
            t.packageName = extension.packageName.ifBlank { this.group.toString() }
            t.className = extension.className.ifBlank { DEFAULT_CLASS_NAME }
            t.fields = extension.fields
            t.outputDir = File("${project.buildDir}/generated/source/buildconfig/java/main")
        }
    }

    private fun Project.configureSourceSets() {
        val buildDir = this.buildDir.toString()

        val ext = this.extensions.findByType(JavaPluginExtension::class.java)
            ?: throw IllegalStateException("Cannot found java plugin extension.")

        ext.sourceSets.configureEach { sourceSet ->
            sourceSet.java.srcDir("$buildDir/generated/source/buildconfig/java/main")
        }
    }

    private fun Project.configureTaskDependency(task: TaskProvider<GenBuildConfigTask>) {
        when {
            this.plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> {
                this.tasks
                    .withType(KotlinCompile::class.java)
                    .configureEach { it.dependsOn(task) }
            }
            this.plugins.hasPlugin(JavaPlugin::class.java) || this.plugins.hasPlugin(JavaLibraryPlugin::class.java) -> {
                this.tasks
                    .withType(JavaCompile::class.java)
                    .configureEach { it.dependsOn(task) }
            }
            else -> throw IllegalStateException("""
                'io.woong.buildconfig' plugin requires 'java', 'java-library' or 'org.jetbrains.kotlin.jvm' plugin.
            """.trimIndent())
        }
    }

    companion object {
        const val EXTENSION_NAME: String = "buildConfig"
        const val TASK_NAME: String = "genBuildConfig"
        const val DEFAULT_CLASS_NAME: String = "BuildConfig"
    }
}

