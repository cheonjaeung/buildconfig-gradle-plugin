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

class BuildConfigExtensionImpl : BuildConfigExtension() {
    val fields: MutableList<Field> = mutableListOf()

    override fun field(name: String, value: Boolean) {
        fields.add(Field(FieldType.BOOLEAN, name, value.toString()))
    }

    override fun field(name: String, value: Byte) {
        fields.add(Field(FieldType.BYTE, name, value.toString()))
    }

    override fun field(name: String, value: Short) {
        fields.add(Field(FieldType.SHORT, name, value.toString()))
    }

    override fun field(name: String, value: Int) {
        fields.add(Field(FieldType.INT, name, value.toString()))
    }

    override fun field(name: String, value: Long) {
        fields.add(Field(FieldType.LONG, name, "${value}L"))
    }

    override fun field(name: String, value: Float) {
        fields.add(Field(FieldType.FLOAT, name, "${value}f"))
    }

    override fun field(name: String, value: Double) {
        fields.add(Field(FieldType.DOUBLE, name, value.toString()))
    }

    override fun field(name: String, value: Char) {
        fields.add(Field(FieldType.CHAR, name, "\'$value\'"))
    }

    override fun field(name: String, value: String) {
        fields.add(Field(FieldType.STRING, name, "\"$value\""))
    }
}
