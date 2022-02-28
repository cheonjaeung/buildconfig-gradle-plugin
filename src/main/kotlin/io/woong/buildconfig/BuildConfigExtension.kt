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

abstract class BuildConfigExtension {
    var packageName: String = ""

    var className: String = ""

    abstract fun field(name: String, value: Boolean)

    abstract fun field(name: String, value: Byte)

    abstract fun field(name: String, value: Short)

    abstract fun field(name: String, value: Int)

    abstract fun field(name: String, value: Long)

    abstract fun field(name: String, value: Float)

    abstract fun field(name: String, value: Double)

    abstract fun field(name: String, value: Char)

    abstract fun field(name: String, value: String)
}
