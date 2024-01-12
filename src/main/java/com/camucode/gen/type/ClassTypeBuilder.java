/*
 * Copyright 2024 Diego Silva <diego.silva at apuntesdejava.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.camucode.gen.type;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
public class ClassTypeBuilder {

    private String packageName;
    private String className;
    private final Map<String, Object> generics = new LinkedHashMap<>();

    public static ClassTypeBuilder newBuilder() {
        return new ClassTypeBuilder();
    }

    public ClassTypeBuilder packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public ClassTypeBuilder className(String className) {
        this.className = className;
        return this;
    }

    public ClassTypeBuilder addGeneric(String key, Object genericClass) {
        generics.put(key, genericClass);
        return this;
    }

    public ClassType build() {
        var classType = new ClassType(packageName, className);
        if (!generics.isEmpty()) {
            classType.generics = generics;
        }
        return classType;
    }
}
